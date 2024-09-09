package com.netease.lowcode.extensions;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class CustomExcelData {
    public String fileName;

    public List<CustomSheetData> sheetList;
}
