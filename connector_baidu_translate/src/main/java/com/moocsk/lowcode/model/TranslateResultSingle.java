package com.moocsk.lowcode.model;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 单条文本翻译结果响应
 */
@NaslStructure
public class TranslateResultSingle {
    /**
     * 错误码
     */
    public String errorCode;

    /**
     * 错误信息
     */
    public String errorMsg;

    /**
     * 源语言
     */
    public String from;

    /**
     * 目标语言
     */
    public String to;

    /**
     * 原文
     */
    public String src;

    /**
     * 译文
     */
    public String dst;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "TranslateResultSingle [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", from=" + from + ", to="
                + to + ", src=" + src + ", dst=" + dst + "]";
    }
    
}
