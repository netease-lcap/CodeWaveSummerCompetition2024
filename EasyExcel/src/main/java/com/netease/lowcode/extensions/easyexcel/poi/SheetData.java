package com.netease.lowcode.extensions.easyexcel.poi;

import java.util.*;

public class SheetData {

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 属性与表头的映射
     */
    private Map<String,String> colHeadMap = new HashMap<>();
    private Map<String,Integer> colHeadIndexMap = new HashMap<>();
    private Map<String,Class<?>> colJavaTypeMap = new HashMap<>();
    private List<MergedRegion> mergedRegionList = new ArrayList<>();
    private Map<String,CellStyle> colStyleMap = new HashMap<>();

    /**
     * 行 数据
     */
    private List<RowData> rowDataList = new ArrayList<>();

    public void putColHeadEntry(String key, String value) {
        this.colHeadMap.put(key, value);
    }

    public void putColJavaTypeEntry(String key, Class<?> clazz) {
        this.colJavaTypeMap.put(key, clazz);
    }

    public void putColHeadIndexEntry(String key, Integer index) {
        this.colHeadIndexMap.put(key, index);
    }

    public void putColStyleEntry(String key, CellStyle style) {
        this.colStyleMap.put(key, style);
    }

    public CellStyle getColStyle(String key) {
        return this.colStyleMap.get(key);
    }

    public Integer getColHeadIndex(String key) {
        return this.colHeadIndexMap.get(key);
    }

    public Class<?> getColJavaType(String key) {
        return this.colJavaTypeMap.getOrDefault(key, Object.class);
    }

    public Set<String> getColNames() {
        return this.colHeadMap.keySet();
    }

    public RowData getRow(int rowNum) {
        return rowDataList.get(rowNum);
    }

    public void addRow(RowData rowData) {
        this.rowDataList.add(rowData);
    }

    public void addMergedRegion(MergedRegion region) {
        this.mergedRegionList.add(region);
    }

    public List<MergedRegion> getMergedRegionList() {
        return mergedRegionList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<RowData> getRowDataList() {
        return rowDataList;
    }

    public void setRowDataList(List<RowData> rowDataList) {
        this.rowDataList = rowDataList;
    }

    public Map<String, String> getColHeadMap() {
        return colHeadMap;
    }

    public void setColHeadMap(Map<String, String> colHeadMap) {
        this.colHeadMap = colHeadMap;
    }

    public Map<String, Integer> getColHeadIndexMap() {
        return colHeadIndexMap;
    }
}
