package com.moocsk.lowcode.tencent.message.model;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 单条短信发送结果
 */
@NaslStructure
public class SendSmsSingleResult {
    /**
     * 唯一请求 ID
     */
    public String RequestId;

    /**
     * 短信发送状态
     */
    public SendStatus SendStatus;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public SendStatus getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        SendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "SendSmsSingleResult [RequestId=" + RequestId + ", SendStatus=" + SendStatus + "]";
    }

}
