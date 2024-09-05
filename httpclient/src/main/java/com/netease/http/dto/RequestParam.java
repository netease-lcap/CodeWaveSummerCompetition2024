package com.netease.http.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.netease.lowcode.core.annotation.Required;

import java.util.Map;

@NaslStructure
public class RequestParam {
    @Required
    public String url;
    @Required
    public String httpMethod;
    @Required
    public Map<String, String> header;
    @Required
    public Map<String, String> body;
    /**
     * 是否忽略证书校验
     */
    @Required
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

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public Boolean getIsIgnoreCrt() {
        return isIgnoreCrt;
    }

    public void setIsIgnoreCrt(Boolean ignoreCrt) {
        isIgnoreCrt = ignoreCrt;
    }

}
