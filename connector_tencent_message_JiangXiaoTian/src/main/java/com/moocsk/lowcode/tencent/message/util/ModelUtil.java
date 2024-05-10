package com.moocsk.lowcode.tencent.message.util;

import java.util.ArrayList;
import java.util.List;

import com.moocsk.lowcode.tencent.message.model.SendSmsResult;
import com.moocsk.lowcode.tencent.message.model.SendSmsSingleResult;
import com.moocsk.lowcode.tencent.message.model.SendStatus;

public class ModelUtil {

    /**
     * 获取单条短信发送结果
     * 
     * @param sendSmsResult 批量短信发送结果
     * @return 单条短信发送结果
     */
    public static SendSmsSingleResult getSendSmsSingleResult(SendSmsResult sendSmsResult) {
        SendSmsSingleResult result = new SendSmsSingleResult();
        result.setRequestId(sendSmsResult.getRequestId());
        result.setSendStatus(sendSmsResult.getSendStatusSet().get(0));
        return result;
    }

    /**
     * 获取低代码平台SendStatus模型数据
     * 
     * @param sendStatus 腾讯 SDK SendStatus模型数据
     * @return 低代码平台SendStatus模型数据
     */
    public static SendStatus getSendStatus(com.tencentcloudapi.sms.v20210111.models.SendStatus sendStatus) {
        SendStatus result = new SendStatus();
        result.setCode(sendStatus.getCode());
        result.setFee(sendStatus.getFee());
        result.setIsoCode(sendStatus.getIsoCode());
        result.setMessage(sendStatus.getMessage());
        result.setPhoneNumber(sendStatus.getPhoneNumber());
        result.setSerialNo(sendStatus.getSerialNo());
        result.setSessionContext(sendStatus.getSessionContext());
        return result;
    }

    /**
     * 获取低代码平台SendStatus模型数据列表
     * 
     * @param sendStatusArray 腾讯 SDK SendStatus模型数据列表
     * @return 代码平台SendStatus模型数据列表
     */
    public static List<SendStatus> getSendStatusList(
            com.tencentcloudapi.sms.v20210111.models.SendStatus[] sendStatusArray) {
        List<SendStatus> result = new ArrayList<SendStatus>();
        for (int i = 0; i < sendStatusArray.length; i++) {
            SendStatus sendStatus = getSendStatus(sendStatusArray[i]);
            result.add(sendStatus);
        }
        return result;
    }

}
