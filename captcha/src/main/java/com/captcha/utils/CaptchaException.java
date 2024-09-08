package com.captcha.utils;

public class CaptchaException extends RuntimeException {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CaptchaException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
