package com.netease.lowcode.lib.dto;

public class RequestHeader {
    private String sign;
    private String timestamp;
    private String nonce;
    private String body;

    public RequestHeader(String sign, String timestamp, String nonce, String body) {
        this.sign = sign;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
