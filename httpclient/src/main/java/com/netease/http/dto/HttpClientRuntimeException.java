package com.netease.http.dto;

import org.springframework.http.HttpStatus;

public class HttpClientRuntimeException extends RuntimeException {
    private int httpCode;
    private String errorKey;
    private transient Object[] args;
    private String message;

    public HttpClientRuntimeException(String errorKey) {
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.errorKey = errorKey;
    }

    public HttpClientRuntimeException(int httpCode, String errorKey) {
        this.httpCode = httpCode;
        this.errorKey = errorKey;
    }

    public HttpClientRuntimeException(int httpCode, String errorKey, Object... args) {
        this.httpCode = httpCode;
        this.errorKey = errorKey;
        this.args = args;
    }

    public HttpClientRuntimeException(int httpCode, Throwable t) {
        super(t);
        this.httpCode = httpCode;
        this.errorKey = t.getMessage();
    }

    public HttpClientRuntimeException(String errorKey, Throwable t) {
        super(t);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.errorKey = errorKey;
    }

    public HttpClientRuntimeException(int httpCode, String errorKey, Throwable t) {
        super(t);
        this.httpCode = httpCode;
        this.errorKey = errorKey;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
