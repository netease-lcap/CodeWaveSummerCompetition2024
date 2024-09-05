package com.fdddf.tilemap;

import com.netease.lowcode.core.annotation.NaslStructure;


@NaslStructure
public class TileValidateResponse {
    /**
     * 是否合法
     */
    public Boolean isValid;
    /**
     * 错误码
     */
    public Long errorCode;
    /**
     * 错误原因
     */
    public String reason;

    /**
     * 提示
     */
    public String tips;

    public TileValidateResponse() {
    }

    public TileValidateResponse(ErrorCode err) {
        this.isValid = err == ErrorCode.SUCCESS;
        this.errorCode = err.code;
        this.reason = err.message;
    }

    public void setMessage(String message) {
        this.reason = message;
    }
}
