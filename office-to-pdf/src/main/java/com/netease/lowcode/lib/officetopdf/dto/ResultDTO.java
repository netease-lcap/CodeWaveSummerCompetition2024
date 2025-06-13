package com.netease.lowcode.lib.officetopdf.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class ResultDTO {
    /**
     * 文件key
     */
    public String key;
    /**
     * 文件url
     */
    public String fileUrl;
    /**
     * 状态 0-成功 1-处理中 2-不存在
     */
    public Integer status;
    /**
     * 真实ppt总页数
     */
    public Long realCount;
    /**
     * 访问路径
     */
    public List<String> pageUrls;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public List<String> getPageUrls() {
        return pageUrls;
    }

    public void setPageUrls(List<String> pageUrls) {
        this.pageUrls = pageUrls;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
