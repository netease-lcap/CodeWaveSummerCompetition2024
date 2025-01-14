package com.netease.lowcode.lib.officetopdf.dto;

public class FileTransferRequest {
    /**
     * 文件url
     */
    private String fileUrl;
    /**
     * 文件类型, 1. ppt
     */
    private Integer type;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}