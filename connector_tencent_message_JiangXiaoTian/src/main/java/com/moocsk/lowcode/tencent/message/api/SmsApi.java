package com.moocsk.lowcode.tencent.message.api;

import java.util.ArrayList;
import java.util.List;

import com.moocsk.lowcode.tencent.message.model.SendSmsResult;
import com.moocsk.lowcode.tencent.message.util.ModelUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.DescribePhoneNumberInfoRequest;
import com.tencentcloudapi.sms.v20210111.models.DescribePhoneNumberInfoResponse;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

public class SmsApi {

    /** 短信相关接口请求地址 */
    private static final String SEND_SMS = "sms.tencentcloudapi.com";

    /** AppId */
    private String secretId;

    /** 密钥 */
    private String secretKey;

    /**
     * @param secretId  应用 ID
     * @param secretKey 应用密钥
     */
    public SmsApi(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    /**
     * 发送短信
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
     * @return 短信发送结果
     */
    public SendSmsResult sendSms(String Region, List<String> PhoneNumberSet, String SmsSdkAppId, String TemplateId,
            String SignName, List<String> TemplateParamSet, String ExtendCode, String SessionContext, String SenderId) {
        SendSmsResult smsResult = new SendSmsResult();
        // 防止手机号码列表为空
        String[] NewPhoneNumberSet = new String[0];
        if (PhoneNumberSet != null) {
            NewPhoneNumberSet = PhoneNumberSet.toArray(new String[PhoneNumberSet.size()]);
        }
        // 参数列表不为空，转为字符串数组
        String[] NewTemplateParamSet = null;
        if (TemplateParamSet != null) {
            NewTemplateParamSet = TemplateParamSet.toArray(new String[TemplateParamSet.size()]);
        }
        try {
            Credential cred = this.getCredential();
            ClientProfile clientProfile = this.getSendSmsClientProfile();
            SmsClient client = new SmsClient(cred, Region, clientProfile);
            SendSmsRequest req = new SendSmsRequest();
            req.setPhoneNumberSet(NewPhoneNumberSet);
            req.setSmsSdkAppId(SmsSdkAppId);
            req.setTemplateId(TemplateId);
            req.setSignName(SignName);
            req.setTemplateParamSet(NewTemplateParamSet);
            req.setExtendCode(ExtendCode);
            req.setSessionContext(SessionContext);
            req.setSenderId(SenderId);
            SendSmsResponse res = client.SendSms(req);
            smsResult.setRequestId(res.getRequestId());
            smsResult.setSendStatusSet(ModelUtil.getSendStatusList(res.getSendStatusSet()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return smsResult;
    }

    /**
     * 查询号码信息（目前该接口用于联通性测试）
     * 
     * @param Region         地域列表
     * @param phoneNumberSet 下发手机号码列表
     * @return 手机号码信息
     */
    public DescribePhoneNumberInfoResponse getPhoneNumberInfo(String Region, String[] phoneNumberSet) {
        try {
            Credential cred = this.getCredential();
            ClientProfile clientProfile = this.getSendSmsClientProfile();
            SmsClient client = new SmsClient(cred, Region, clientProfile);
            DescribePhoneNumberInfoRequest req = new DescribePhoneNumberInfoRequest();
            req.setPhoneNumberSet(phoneNumberSet);
            DescribePhoneNumberInfoResponse res = client.DescribePhoneNumberInfo(req);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ClientProfile getSendSmsClientProfile() {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(SEND_SMS);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return clientProfile;
    }

    private Credential getCredential() {
        Credential cred = new Credential(this.secretId, this.secretKey);
        return cred;
    }

}
