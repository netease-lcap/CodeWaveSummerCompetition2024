package com.netease.lowcode.extensions.response;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.Map;

@NaslStructure
public class ExportBigDataResponse {

    public Boolean success;
    public String msg;
    public String trace;

    public String filePath;
    public String url;
    public Double cost;// 处理耗时,单位 s
    public Double size;// 文件大小,单位字节
    public Long total;
    public Map<String,String> extensionParams;

    public static ExportBigDataResponse FAIL(String msg) {
        return FAIL(msg, null);
    }

    public static ExportBigDataResponse FAIL(String msg, String trace) {
        ExportBigDataResponse fail = new ExportBigDataResponse();
        fail.setSuccess(false);
        fail.setMsg(msg);
        fail.setTrace(trace);
        return fail;
    }

    public static ExportBigDataResponse FAIL2(String msg, Map<String, String> extensionParams) {
        ExportBigDataResponse fail = FAIL(msg, null);
        fail.setExtensionParams(extensionParams);
        return fail;
    }

    public static ExportBigDataResponse FAIL(String msg, String trace, Map<String, String> extensionParams) {
        ExportBigDataResponse fail = FAIL(msg, trace);
        fail.setExtensionParams(extensionParams);
        return fail;
    }

    public static ExportBigDataResponse OK(String msg) {
        return OK(null, null, msg);
    }

    public static ExportBigDataResponse OK(String path, String url) {
        // data
        return OK(path, url, "call success");
    }

    public static ExportBigDataResponse OK(String path, String url, Double cost, Double size) {
        ExportBigDataResponse callSuccess = OK(path, url, "call success");
        callSuccess.setCost(cost);
        callSuccess.setSize(size);
        return callSuccess;
    }

    public static ExportBigDataResponse OK(String path, String url, Double cost, Double size, Map<String, String> extensionParams) {
        ExportBigDataResponse ok = OK(path, url, cost, size);
        ok.setExtensionParams(extensionParams);
        return ok;
    }

    public static ExportBigDataResponse OK(String path,String url,String msg) {
        ExportBigDataResponse ok = new ExportBigDataResponse();
        ok.setSuccess(true);
        ok.setMsg(msg);
        ok.setFilePath(path);
        ok.setUrl(url);
        return ok;
    }

    public ExportBigDataResponse total(Long total) {
        this.setTotal(total);
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Map<String, String> getExtensionParams() {
        return extensionParams;
    }

    public void setExtensionParams(Map<String, String> extensionParams) {
        this.extensionParams = extensionParams;
    }
}

