package com.wgx.lowcode.validation;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * @Date: 2024/3/17 - 03 - 17 - 0:22
 * @Description: com.netease.lowcode.validation
 * @version: 1.0
 */
@NaslStructure
public class RegexValidatorResult {

    public Boolean isValid;
    public String errorMessage;

    public RegexValidatorResult() {
    }

    public RegexValidatorResult(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取
     * @return isValid
     */
    public Boolean isIsValid() {
        return isValid;
    }

    /**
     * 设置
     * @param isValid
     */
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * 获取
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return "RegexValidatorResult{isValid = " + isValid + ", errorMessage = " + errorMessage + "}";
    }
}
