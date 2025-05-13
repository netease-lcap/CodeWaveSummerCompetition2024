package com.netease.lowcode.lib.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.Map;

@NaslStructure
public class RequestInfoStructure {
    public String body;
    public Map<String, String> headersMap;

    public RequestInfoStructure() {
    }

    public RequestInfoStructure(String body, Map<String, String> headersMap) {
        this.body = body;
        this.headersMap = headersMap;
    }

    public Map<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
