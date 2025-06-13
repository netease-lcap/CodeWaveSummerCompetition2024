package com.netease.lowcode.extensions.easyexcel.poi;

import java.util.List;

public class ExcelData {

    /**
     * 文件名 带后缀
     */
    private String name;

    /**
     * sheet列表 按顺序生成
     */
    private List<SheetData> sheetList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SheetData> getSheetList() {
        return sheetList;
    }

    public void setSheetList(List<SheetData> sheetList) {
        this.sheetList = sheetList;
    }
}
