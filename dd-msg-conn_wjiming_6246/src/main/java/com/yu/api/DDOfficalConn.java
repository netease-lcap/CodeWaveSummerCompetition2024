package com.yu.api;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.taobao.api.ApiException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.FileItem;
import com.yu.dto.BaseResultDto;
import com.yu.dto.GetSendProgressDto;
import com.yu.dto.GetSendResultDto;
import com.yu.dto.SendMsgResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wjm
 * @version 1.0.0
 * @date 2024/4/19 14:44
 **/
@NaslConnector(connectorKind = "dd-msg-connector")
public class DDOfficalConn {
    public static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final Logger log = LoggerFactory.getLogger(DDOfficalConn.class);
    /**
     * 也叫 Client ID或 AppKey 或 SuiteKey
     */
    private String appKey;
    /**
     * 也叫 Client Secret 或 AppSecret 或 SuiteSecret
     */
    private String appSecret;
    /**
     * 原企业内部应用AgentId
     */
    private Long agentId;
    private String accessToken;

    /**
     * 对中文字符进行UTF-8编码
     *
     * @param source 要转义的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String tranformStyle(String source) throws UnsupportedEncodingException {
        char[] arr = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char temp = arr[i];
            if (isChinese(temp)) {
                sb.append(URLEncoder.encode("" + temp, "UTF-8"));
                continue;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 判断是不是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    @NaslConnector.Creator
    public DDOfficalConn init(String appKey, String appSecret, String agentId) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.agentId = Long.parseLong(agentId);
        this.accessToken = getAccessToken();
        //开启一个定时任务定时刷新token防止token过期
        scheduledExecutorService.scheduleAtFixedRate(() -> this.accessToken = getAccessToken(), 1, 1, TimeUnit.HOURS);
        return this;
    }

    @NaslConnector.Tester
    public Boolean connectTest(String appKey, String appSecret, String agentId) {
        init(appKey, appSecret, agentId);
        return true;
    }

    public String getAccessToken() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(appKey);
        req.setAppsecret(appSecret);
        req.setHttpMethod("GET");
        try {
            OapiGettokenResponse rsp = client.execute(req);
            String accessToken = rsp.getAccessToken();
            if (accessToken == null) {
                String errorMsg = "获取钉钉accessToken失败：" + rsp.getBody();
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            return accessToken;
        } catch (Exception ex) {
            log.error("获取accessToken异常,其他异常", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 发送文本工作通知
     *
     * @param content    文本内容
     * @param toAllUser  是否全体发送
     * @param deptIdList 部门列表 英文逗号分隔
     * @param useridList 用户列表 英文逗号分隔
     * @return
     */
    @NaslConnector.Logic
    public SendMsgResultDto sendTxtMsg(String content, Boolean toAllUser, String deptIdList, String useridList) throws IllegalArgumentException {
        if (isEmpty(content)) throw new IllegalArgumentException("内容能为空");
        if (toAllUser == null && isEmpty(deptIdList) && isEmpty(useridList))
            throw new IllegalArgumentException("toAllUser和deptIdList和useridList不能同时为空，至少需要指定一个有效发送人");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(this.agentId);
        request.setUseridList(useridList);
        request.setDeptIdList(deptIdList);
        request.setToAllUser(toAllUser);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent(content);
        request.setMsg(msg);
        return execute(new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"), request);
    }

    /**
     * 发送图片工作通知
     *
     * @param mediaId    图片媒体文件id
     * @param toAllUser  是否全体发送
     * @param deptIdList 部门列表 英文逗号分隔
     * @param useridList 用户列表 英文逗号分隔
     * @return
     */
    @NaslConnector.Logic
    public SendMsgResultDto sendImgMsg(String mediaId, Boolean toAllUser, String deptIdList, String useridList) throws IllegalArgumentException {
        if (isEmpty(mediaId)) throw new IllegalArgumentException("图片id 不能为空");
        if (toAllUser == null && isEmpty(deptIdList) && isEmpty(useridList))
            throw new IllegalArgumentException("toAllUser和deptIdList和useridList不能同时为空，至少需要指定一个有效发送人");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(this.agentId);
        request.setUseridList(useridList);
        request.setDeptIdList(deptIdList);
        request.setToAllUser(toAllUser);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("image");
        msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
        msg.getImage().setMediaId(mediaId);
        request.setMsg(msg);
        return execute(new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"), request);

    }

    public SendMsgResultDto execute(DingTalkClient client, BaseTaobaoRequest<OapiMessageCorpconversationAsyncsendV2Response> request) {
        try {
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, this.accessToken);
            SendMsgResultDto resultDto = JSON.parseObject(rsp.getBody(), SendMsgResultDto.class);
            if (resultDto.errcode != 0) {
                String errMsg = "工作通知接口调用失败：" + rsp.getBody();
                log.warn(errMsg);
                throw new RuntimeException();
            }
            return resultDto;
        } catch (ApiException e) {
            log.error("发送工作通知失败：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送OA消息
     *
     * @param useridList 用户列表 不得超过5个
     * @param msgBody    消息体 例如 {"message_url":"http://dingtalk.com","head":{"bgcolor":"FFBBBBBB","text":"头部标题"},"body":{"title":"正文标题","form":[{"key":"姓名:","value":"张三"},{"key":"年龄:","value":"20"},{"key":"身高:","value":"1.8米"},{"key":"体重:","value":"130斤"},{"key":"学历:","value":"本科"},{"key":"爱好:","value":"打球、听音乐"}],"rich":{"num":"15.6","unit":"元"},"content":"大段文本大段文本大段文本大段文本大段文本大段文本","image":"@lADOADmaWMzazQKA","file_count":"3","author":"李四 "}}
     * @return
     */
    @NaslConnector.Logic
    public SendMsgResultDto sendOAMsg(String useridList, String msgBody) throws IllegalArgumentException {
        if (isEmpty(useridList) || isEmpty(msgBody)) throw new IllegalArgumentException("用户id列表和消息内容不能为空");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(this.agentId);
        request.setUseridList(useridList);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        OapiMessageCorpconversationAsyncsendV2Request.OA oaMsg = JSONObject.parseObject(msgBody, OapiMessageCorpconversationAsyncsendV2Request.OA.class);
        msg.setOa(oaMsg);
        msg.setMsgtype("oa");
        request.setMsg(msg);
        return execute(new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"), request);
    }

    private boolean isEmpty(String cs) {
        return cs == null || cs.trim().length() == 0;
    }

    /**
     * 上传钉钉媒体文件
     *
     * @param filePath 互联网文件路径
     * @return
     */
    @NaslConnector.Logic
    public String uploadFile(String filePath) throws IllegalArgumentException {
        if (isEmpty(filePath)) throw new IllegalArgumentException("filePath 不能为空");
        InputStream inputStream = null;
        try {
            String encodedUrl = tranformStyle(filePath);
            URL url = new URL(encodedUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            byte[] fileBytes = IoUtil.readBytes(inputStream);
            String name = FileUtil.getName(filePath);
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/media/upload");
            OapiMediaUploadRequest req = new OapiMediaUploadRequest();
            req.setType("image");
            // 要上传的媒体文件
            FileItem item = new FileItem(name, fileBytes);
            req.setMedia(item);
            OapiMediaUploadResponse rsp = client.execute(req, this.accessToken);
            if (rsp.getErrcode() != 0) {
                String errMsg = "上传文件接口失败：" + rsp.getBody();
                log.warn(errMsg);
                throw new RuntimeException();
            }
            return rsp.getMediaId();
        } catch (ApiException e) {
            log.error("上传钉钉文件失败：：", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("获取图片失败：", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取工作通知消息的发送进度
     *
     * @param taskId 通知发送成功后返回的任务ID
     * @return
     */
    @NaslConnector.Logic
    public GetSendProgressDto getSendProgress(Long taskId) throws IllegalArgumentException {
        if (taskId == null) throw new IllegalArgumentException("taskId不能为空");
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress");
        OapiMessageCorpconversationGetsendprogressRequest req = new OapiMessageCorpconversationGetsendprogressRequest();
        req.setAgentId(this.agentId);
        req.setTaskId(taskId);
        try {
            OapiMessageCorpconversationGetsendprogressResponse rsp = client.execute(req, accessToken);
            if (rsp.getErrcode() != 0) {
                String errMsg = "获取工作通知发送进度失败：" + rsp.getBody();
                log.warn(errMsg);
                throw new RuntimeException();
            }
            return JSON.parseObject(rsp.getBody(), GetSendProgressDto.class);
        } catch (ApiException e) {
            log.error("获取消息发送进度失败：：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取工作通知的发送结果
     *
     * @param taskId 通知发送成功后返回的任务ID
     * @return
     */
    @NaslConnector.Logic
    public GetSendResultDto getSendResult(Long taskId) throws IllegalArgumentException {
        if (taskId == null) throw new IllegalArgumentException("taskId不能为空");
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult");
        OapiMessageCorpconversationGetsendresultRequest req = new OapiMessageCorpconversationGetsendresultRequest();
        req.setAgentId(this.agentId);
        req.setTaskId(taskId);
        OapiMessageCorpconversationGetsendresultResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            if (rsp.getErrcode() != 0) {
                String errMsg = "获取工作通知发送结果接口失败：" + rsp.getBody();
                log.warn(errMsg);
                throw new RuntimeException();
            }
            return JSON.parseObject(rsp.getBody(), GetSendResultDto.class);
        } catch (ApiException e) {
            log.error("获取消息发送结果失败：：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 撤回工作消息通知，只能撤回24小时内发送的工作通知消息。
     *
     * @param msgTaskId 通知发送成功后返回的任务ID
     * @return
     */
    @NaslConnector.Logic
    public BaseResultDto recallMsg(Long msgTaskId) throws IllegalArgumentException {
        if (msgTaskId == null) throw new IllegalArgumentException("msgTaskId不能为空");
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/recall");
        OapiMessageCorpconversationRecallRequest req = new OapiMessageCorpconversationRecallRequest();
        req.setAgentId(this.agentId);
        req.setMsgTaskId(msgTaskId);
        OapiMessageCorpconversationRecallResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            if (rsp.getErrcode() != 0) {
                String errMsg = "撤回工作通知消息接口失败：" + rsp.getBody();
                log.warn(errMsg);
                throw new RuntimeException();
            }
            return JSON.parseObject(rsp.getBody(), BaseResultDto.class);
        } catch (ApiException e) {
            log.error("撤回工作消息失败：：", e);
            throw new RuntimeException(e);
        }
    }

}
