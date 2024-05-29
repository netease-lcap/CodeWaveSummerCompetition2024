package com.netease.lowcode.extensions;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class CustomSheetData {
    public String sheetName;
    public List<String> head;

    /**
     * 若数据为图片链接，若要将图片展示在excel中，须给此数据加上codewave_excel_pic:前缀
     */
    public List<List<String>> data;
}
