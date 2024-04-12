package com.moocsk.lowcode.model;

import java.util.List;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 批量文本翻译结果响应
 */
@NaslStructure
public class TranslateResult {
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
     * 翻译结果
     */
    public List<Translate> transResult;

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

    public List<Translate> getTransResult() {
        return transResult;
    }

    public void setTransResult(List<Translate> transResult) {
        this.transResult = transResult;
    }

    @Override
    public String toString() {
        return "TranslateResult [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", from=" + from + ", to=" + to
                + ", transResult=" + transResult + "]";
    }
    
}
