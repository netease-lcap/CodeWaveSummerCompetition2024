package com.netease.http.dto;

import java.util.Map;

public class RequestParamAllBodyTypeInner<T> {

    public String url;

    public String httpMethod;

    public Map<String, String> header;

    public T body;
    /**
     * 是否忽略证书校验
     */

    public Boolean isIgnoreCrt;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Boolean getIsIgnoreCrt() {
        return isIgnoreCrt;
    }

    public void setIsIgnoreCrt(Boolean ignoreCrt) {
        isIgnoreCrt = ignoreCrt;
    }
}
