package com.netease.lowcode.extensions;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;
import java.util.Map;

@NaslStructure
public class ParseSheetResult {

    public String sheetName;
    public Integer sheetNo;

    public List<List<String>> headList;

    public List<List<String>> dataList;

    public String errorMsg;//错误信息
    public Boolean success;// 是否成功

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public List<List<String>> getHeadList() {
        return headList;
    }

    public void setHeadList(List<List<String>> headList) {
        this.headList = headList;
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
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
