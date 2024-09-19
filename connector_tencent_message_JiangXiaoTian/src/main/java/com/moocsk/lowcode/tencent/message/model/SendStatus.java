package com.moocsk.lowcode.tencent.message.model;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 短信发送状态
 */
@NaslStructure
public class SendStatus {

    /**
     * 发送流水号
     */
    public String SerialNo;

    /**
     * 手机号码，E.164标准，+[国家或地区码][手机号]
     */
    public String PhoneNumber;

    /**
     * 计费条数
     */
    public Long Fee;

    /**
     * 用户 session 内容
     */
    public String SessionContext;

    /**
     * 短信请求错误码，发送成功返回 "Ok"
     */
    public String Code;

    /**
     * 短信请求错误码描述
     */
    public String Message;

    /**
     * 国家码或地区码，对于未识别出国家码或者地区码，默认返回 DEF
     */
    public String IsoCode;

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Long getFee() {
        return Fee;
    }

    public void setFee(Long fee) {
        Fee = fee;
    }

    public String getSessionContext() {
        return SessionContext;
    }

    public void setSessionContext(String sessionContext) {
        SessionContext = sessionContext;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getIsoCode() {
        return IsoCode;
    }

    public void setIsoCode(String isoCode) {
        IsoCode = isoCode;
    }

    @Override
    public String toString() {
        return "SendStatus [SerialNo=" + SerialNo + ", PhoneNumber=" + PhoneNumber + ", Fee=" + Fee
                + ", SessionContext=" + SessionContext + ", Code=" + Code + ", Message=" + Message + ", IsoCode="
                + IsoCode + "]";
    }

}
