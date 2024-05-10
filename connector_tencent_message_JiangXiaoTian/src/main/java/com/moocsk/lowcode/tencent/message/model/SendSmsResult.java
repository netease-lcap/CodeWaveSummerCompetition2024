package com.moocsk.lowcode.tencent.message.model;

import java.util.List;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 批量短信发送结果
 */
@NaslStructure
public class SendSmsResult {

    /**
     * 唯一请求 ID
     */
    public String RequestId;

    /**
     * 短信发送状态
     */
    public List<SendStatus> SendStatusSet;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public List<SendStatus> getSendStatusSet() {
        return SendStatusSet;
    }

    public void setSendStatusSet(List<SendStatus> sendStatusSet) {
        SendStatusSet = sendStatusSet;
    }

    @Override
    public String toString() {
        return "SendSmsResult [RequestId=" + RequestId + ", SendStatusSet=" + SendStatusSet + "]";
    }

}
