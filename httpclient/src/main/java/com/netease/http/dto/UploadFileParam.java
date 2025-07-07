package com.netease.http.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.netease.lowcode.core.annotation.Required;

import java.util.Map;

@NaslStructure
public class UploadFileParam {
    @Required
    public String fileUrl;
    /**
     *  form-data请求中文件的key，默认:file
     */
    @Required
    public String fileKey;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
