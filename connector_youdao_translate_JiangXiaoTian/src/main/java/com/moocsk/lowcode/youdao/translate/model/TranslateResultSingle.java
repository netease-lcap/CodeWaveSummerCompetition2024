package com.moocsk.lowcode.youdao.translate.model;

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
    public String query;

    /**
     * 译文
     */
    public String translation;

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "{ errorCode: " + errorCode + ", errorMsg: " + errorMsg + ", from: " + from + ", to: "
                + to + ", query: " + query + ", translation: " + translation + " }";
    }

}
