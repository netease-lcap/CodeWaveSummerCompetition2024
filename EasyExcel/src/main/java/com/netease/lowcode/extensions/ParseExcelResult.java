package com.netease.lowcode.extensions;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.Map;

@NaslStructure
public class ParseExcelResult {

    public Map<String, ParseSheetResult> sheetMap;//sheet数据 key=sheetName

    public String errorMsg;//错误信息
    public Boolean success;// 是否成功

    public Map<String, ParseSheetResult> getSheetMap() {
        return sheetMap;
    }

    public void setSheetMap(Map<String, ParseSheetResult> sheetMap) {
        this.sheetMap = sheetMap;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
