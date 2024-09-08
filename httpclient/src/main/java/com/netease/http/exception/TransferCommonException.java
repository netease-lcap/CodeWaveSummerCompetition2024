package com.netease.http.exception;

public class TransferCommonException extends RuntimeException {
    private String message;

    public TransferCommonException(int httpStatus, String responseBody) {
        this.message = "httpStatus:" + httpStatus + ",responseBody:" + responseBody;
    }

    public TransferCommonException(String responseBody, Throwable t) {
        super(t);
        this.message = responseBody;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
