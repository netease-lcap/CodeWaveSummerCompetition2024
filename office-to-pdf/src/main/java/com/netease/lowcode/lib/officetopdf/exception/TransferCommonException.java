package com.netease.lowcode.lib.officetopdf.exception;

public class TransferCommonException extends RuntimeException {
    private int errorCode;
    private transient Object[] args;
    private String message;

    private Throwable t;

    public TransferCommonException(int errorCode, String message, Object... args) {
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public TransferCommonException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
