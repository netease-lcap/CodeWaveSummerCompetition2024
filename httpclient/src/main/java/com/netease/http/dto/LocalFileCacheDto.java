package com.netease.http.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class LocalFileCacheDto {
    public String resBody;
    public String fileName;
    /**
     * 文件下载状态，0-不存在，1-文件下载中，2-本地已下载完成，待上传，3-上传中，4-上传完成,5-文件下载失败,6-文件上传失败
     */
    public Integer downloadStatus;

    public LocalFileCacheDto() {
    }

    public LocalFileCacheDto(String resBody, String fileName, Integer downloadStatus) {
        this.resBody = resBody;
        this.fileName = fileName;
        this.downloadStatus = downloadStatus;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(Integer downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
