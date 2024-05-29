package com.netease.lowcode.extensions;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ExcelResponse {

    public Boolean success;
    public String msg;
    public String data;
    public String trace;

    public static ExcelResponse OK(String data) {
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setSuccess(true);
        excelResponse.setData(data);
        return excelResponse;
    }

    public static ExcelResponse FAIL(String msg, String trace) {
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setSuccess(false);
        excelResponse.setMsg(msg);
        excelResponse.setTrace(trace);
        return excelResponse;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }
}

