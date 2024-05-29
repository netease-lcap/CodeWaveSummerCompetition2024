package com.moocsk.lowcode.tencent.message.connector;

import java.util.ArrayList;
import java.util.List;

import com.moocsk.lowcode.tencent.message.api.SmsApi;
import com.moocsk.lowcode.tencent.message.model.SendSmsResult;
import com.moocsk.lowcode.tencent.message.model.SendSmsSingleResult;
import com.moocsk.lowcode.tencent.message.util.ModelUtil;
import com.moocsk.lowcode.tencent.message.util.StringUtil;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.tencentcloudapi.sms.v20210111.models.DescribePhoneNumberInfoResponse;

/**
 * 腾讯信息连接器
 */
@NaslConnector(connectorKind = "TencentMessage")
public class SmsMessage {

    /** 应用 Id */
    private String secretId;

    /** 密钥 */
    private String secretKey;

    /**
     * 初始化
     * 
     * @param appid     应用ID
     * @param secretKey 密钥
     * @return 腾讯信息连接器
     */
    @NaslConnector.Creator
    public SmsMessage init(String secretId, String secretKey) {
        SmsMessage tencentMessage = new SmsMessage();
        tencentMessage.secretId = secretId;
        tencentMessage.secretKey = secretKey;
        return tencentMessage;
    }

    /**
     * 连通性测试
     * 
     * @param secretId  应用ID
     * @param secretKey 密钥
     * @return 连通结果
     */
    @NaslConnector.Tester
    public Boolean testConnection(String secretId, String secretKey) {
        SmsApi smsApi = new SmsApi(secretId, secretKey);
        DescribePhoneNumberInfoResponse res = smsApi.getPhoneNumberInfo("ap-beijing",
                new String[] { "+8618843210001" });
        String Code = res.getPhoneNumberInfoSet()[0].getCode();
        if ("Ok".equals(Code)) {
            return true;
        }
        return false;
    }

    /**
     * 单条短信发送
     * 
     * @param Region           地域列表
     * @param PhoneNumber      下发手机号码
     * @param SmsSdkAppId      短信 SdkAppId
     * @param TemplateId       模板 ID
     * @param SignName         短信签名内容
     * @param TemplateParamSet 模板参数
     * @param ExtendCode       短信码号扩展号
     * @param SessionContext   用户的 session 内容
     * @param SenderId         SenderId
     * @return 单条短信发送结果
     */
    @NaslConnector.Logic
    public SendSmsSingleResult sendSms(String Region, String PhoneNumber, String SmsSdkAppId, String TemplateId,
            String SignName, List<String> TemplateParamSet, String ExtendCode, String SessionContext, String SenderId) {
        SendSmsSingleResult result = new SendSmsSingleResult();
        try {
            if (StringUtil.getByteLength(SessionContext) > 512) {
                throw new RuntimeException("SessionContext长度需小于 512 字节");
            }
            SmsApi smsApi = new SmsApi(this.secretId, this.secretKey);
            List<String> PhoneNumberSet = new ArrayList<String>();
            PhoneNumberSet.add(PhoneNumber);
            SendSmsResult ssResult = smsApi.sendSms(Region, PhoneNumberSet, SmsSdkAppId, TemplateId, SignName,
                    TemplateParamSet,
                    ExtendCode, SessionContext, SenderId);
            result = ModelUtil.getSendSmsSingleResult(ssResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    /**
     * 批量短信发送
     * 
     * @param Region           地域列表
     * @param PhoneNumberSet   下发手机号码列表
     * @param SmsSdkAppId      短信 SdkAppId
     * @param TemplateId       模板 ID
     * @param SignName         短信签名内容
     * @param TemplateParamSet 模板参数
     * @param ExtendCode       短信码号扩展号
     * @param SessionContext   用户的 session 内容
     * @param SenderId         SenderId
     * @return 批量短信发送结果
     */
    @NaslConnector.Logic
    public SendSmsResult sendSmsBatch(String Region, List<String> PhoneNumberSet, String SmsSdkAppId, String TemplateId,
            String SignName, List<String> TemplateParamSet, String ExtendCode, String SessionContext, String SenderId) {
        SendSmsResult result = new SendSmsResult();
        try {
            if (PhoneNumberSet != null && PhoneNumberSet.size() > 200) {
                throw new RuntimeException("单次请求最多支持 200 个手机号");
            }
            if (StringUtil.getByteLength(SessionContext) > 512) {
                throw new RuntimeException("SessionContext长度需小于 512 字节");
            }
            SmsApi smsApi = new SmsApi(this.secretId, this.secretKey);
            result = smsApi.sendSms(Region, PhoneNumberSet, SmsSdkAppId, TemplateId, SignName, TemplateParamSet,
                    ExtendCode, SessionContext, SenderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public static void main(String[] args) {
        String secretId = "";
        String secretKey = "";

        String region = "ap-beijing";
        String PhoneNumber = "";
        String SmsSdkAppId = "";
        String TemplateId = "";
        String SignName = "";
        List<String> TemplateParamSet = new ArrayList<String>();
        TemplateParamSet.add("123456");
        String ExtendCode = "";
        String SessionContext = "";
        String SenderId = "";

        SmsMessage tencentMessage = new SmsMessage().init(secretId, secretKey);

        // 测试连通性
        Boolean connBoolean = tencentMessage.testConnection(secretId, secretKey);
        if (connBoolean) {
            System.out.println("连接成功");
        } else {
            System.out.println("连接失败");
        }

        // 发送单条短信
        SendSmsSingleResult result = tencentMessage.sendSms(region, PhoneNumber,
                SmsSdkAppId, TemplateId, SignName,
                TemplateParamSet, ExtendCode,
                SessionContext, SenderId);
        System.out.println(result);

        // 发送批量短信
        List<String> PhoneNumberSet = new ArrayList<>();
        PhoneNumberSet.add("");
        PhoneNumberSet.add("");
        SendSmsResult results = tencentMessage.sendSmsBatch(region, PhoneNumberSet,
                SmsSdkAppId, TemplateId, SignName,
                TemplateParamSet, ExtendCode,
                SessionContext, SenderId);
        System.out.println(results);

    }

}
