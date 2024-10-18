package com.netease.lowcode.extensions.easyexcel.poi;

import com.netease.lowcode.core.annotation.NaslStructure;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@NaslStructure
public class POIExcelCreateDTO {

    public String exportFileName;
    public String jsonData;
    /**
     * IDE中编辑Label最多只能63个字符，如果超出限制，可在这里传入
     * key=结构或实体的 属性名
     * val=结构或实体的 属性标题
     */
    public Map<String,String> labels;

    public void validate() {
        if (StringUtils.isBlank(exportFileName)) {
            throw new RuntimeException("文件名不能为空");
        }
        if (StringUtils.isBlank(jsonData)) {
            throw new RuntimeException("数据不能为空");
        }
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
}
