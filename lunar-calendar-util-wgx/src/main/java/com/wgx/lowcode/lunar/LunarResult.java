package com.wgx.lowcode.lunar;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * @Date: 2024/4/14 - 04 - 14 - 23:50
 * @Description: com.wgx.lowcode.util
 * @version: 1.0
 */
@NaslStructure
public class LunarResult {
    public Boolean success;
    public String lunarData;
    public String messageText ;

    public LunarResult() {
    }

    public LunarResult(Boolean success, String lunarData, String messageText) {
        this.success = success;
        this.lunarData = lunarData;
        this.messageText = messageText;
    }

    /**
     * 获取
     * @return success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置
     * @param success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 获取
     * @return lunarData
     */
    public String getLunarData() {
        return lunarData;
    }

    /**
     * 设置
     * @param lunarData
     */
    public void setLunarData(String lunarData) {
        this.lunarData = lunarData;
    }

    /**
     * 获取
     * @return messageText
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * 设置
     * @param messageText
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String toString() {
        return "LunarResult{success = " + success + ", lunarData = " + lunarData + ", messageText = " + messageText + "}";
    }
}
