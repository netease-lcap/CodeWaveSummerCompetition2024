package com.lowcode.netease.connector;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lowcode.netease.api.TransApi;
import com.lowcode.netease.model.TranslateResult;
import com.lowcode.netease.utils.ModelUtil;
import com.netease.lowcode.core.annotation.NaslConnector;

/**
 * 百度翻译连接器
 */
@NaslConnector(connectorKind = "BaiduTranslate")
public class BaiduTranslate {

    /** 应用ID */
    private String appid;

    /** 密钥 */
    private String appSecret;

    /**
     * 初始化
     * @param appid 应用ID
     * @param appSecret 密钥
     */
    @NaslConnector.Creator
    public BaiduTranslate init(String appid, String appSecret) {
        BaiduTranslate baiduTranslate = new BaiduTranslate();
        baiduTranslate.appid = appid;
        baiduTranslate.appSecret = appSecret;
        return baiduTranslate;
    }

    /**
     * 连通性测试
     * @param appid 应用ID
     * @param appSecret 密钥
     */
    @NaslConnector.Tester
    public Boolean testConnection(String appid, String appSecret) {
        if (appid != null && appSecret != null) {
            return true;
        }
        return false;
    }

    /**
     * 通用文本翻译
     * @param q 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     */
    @NaslConnector.Logic
    public TranslateResult translate(String q, String from, String to) {
        // 解决应用中\n不能生效
        q = q.replaceAll("\\\\n", "\n");

        TransApi transApi = new TransApi(this.appid, this.appSecret);
        TranslateResult result = new TranslateResult();

        try {
            String response = transApi.getTransResult(q, from, to);
            JSONObject jsonObject = JSONObject.parseObject(response);
            // 如果 http 200，接口返回错误信息
            String errorCode = jsonObject.getString("error_code");
            if (errorCode != null) {
                String errorMsg = jsonObject.getString("error_msg");
                result.setErrorCode(errorCode);
                result.setErrorCode(errorMsg);;
                return result;
            }
            result.setFrom(jsonObject.getString("from"));
            result.setTo(jsonObject.getString("to"));
            JSONArray array = jsonObject.getJSONArray("trans_result");
            result.setTransResult(ModelUtil.getTranslateModels(array));
        } catch (Exception exception) {
            result.setErrorCode("500");
            result.setErrorMsg(exception.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        BaiduTranslate baiduTranslate = new BaiduTranslate().init("", "");
        String q = "开发\n测试";
        TranslateResult result = baiduTranslate.translate(q, "zh", "en");
        System.out.println(result);
     }
    
}
