package com.netease.lowcode.pdf.extension.structures;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class CreateByXlsxRequest {
    public String templateUrl;
    public String jsonData;
    public String exportFileName;
    // 纸张大小，默认A4
    public String pageSize = "A4";
    // 纸张方向，默认纵向
    public Boolean rotate = false;
    // 异步执行，默认false
    public Boolean async = false;
    // [可选]限定读取范围，防止读取大量空白行
    public Integer lastRowNum;
    public String lastColLabel;

    public Boolean getRotate() {
        return rotate;
    }

    public void setRotate(Boolean rotate) {
        this.rotate = rotate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public Integer getLastRowNum() {
        return lastRowNum;
    }

    public void setLastRowNum(Integer lastRowNum) {
        this.lastRowNum = lastRowNum;
    }

    public String getLastColLabel() {
        return lastColLabel;
    }

    public void setLastColLabel(String lastColLabel) {
        this.lastColLabel = lastColLabel;
    }
}
