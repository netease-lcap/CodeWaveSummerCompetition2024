package com.netease.lowcode.freemarker.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class DownloadResponseDTO {

    public String filePath;
    public String result;
    public Boolean success;
    public String msg;
    public String trace;

    public static DownloadResponseDTO OK(String result, String filePath) {
        DownloadResponseDTO responseDTO = new DownloadResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setMsg("success");
        responseDTO.setResult(result);
        responseDTO.setFilePath(filePath);
        return responseDTO;
    }

    public static DownloadResponseDTO FAIL(String msg, String trace) {
        DownloadResponseDTO responseDTO = new DownloadResponseDTO();
        responseDTO.setSuccess(false);
        responseDTO.setMsg(msg);
        responseDTO.setTrace(trace);
        return responseDTO;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
