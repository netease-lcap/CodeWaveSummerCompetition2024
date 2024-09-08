package com.netease.http.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.io.Serializable;

@NaslStructure
public class UploadResponseDTO implements Serializable {
    public Integer code;
    public String msg;
    public String result;
    public String filePath;
    public Boolean success;
    public String trace;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public static UploadResponseDTO FAIL(String msg) {
        return FAIL(msg, null);
    }
    public static UploadResponseDTO FAIL(String msg, String trace) {
        UploadResponseDTO fail = new UploadResponseDTO();
        fail.setSuccess(false);
        fail.setMsg(msg);
        fail.setTrace(trace);
        fail.setCode(500);
        return fail;
    }
    public static UploadResponseDTO OK(String path, String url) {
        UploadResponseDTO callSuccess=new UploadResponseDTO();
        callSuccess.setFilePath(path);
        callSuccess.setSuccess(true);
        callSuccess.setResult(url);
        callSuccess.setCode(200);
        return callSuccess;
    }

}
