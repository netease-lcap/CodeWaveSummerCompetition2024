package com.netease.lowcode.extensions.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;
import java.util.Map;

@NaslStructure
public class QueryCondition {

    /**
     * 查询起始页码，从0开始
     */
    public Integer pageNum;
    /**
     * 查询每页大小
     */
    public Integer pageSize;
    /**
     * 总页数
     */
    public Integer totalPages;

    /**
     * 是否开启异步化，默认同步
     */
    public Boolean isAsync = false;


    /**
     * 其他自定义查询条件
     */
    public Map<String,String> customQueryCondition;

    /**
     * 指定下载的文件名（*.xlsx）
     */
    public String fileName;

    /**
     * 指定每个sheet的最大数据量，
     * 注意如果设置程最大行数注意减去表头行数
     */
    public Integer sheetTotalRows;

    /**
     * 设置表头,复杂表头设置多行
     * 举例复杂表头：
     * |          主标题1         |   主标题2   |
     * |   子标题1   |   子标题2   |   子标题3   |
     * |   数据a    |    数据b    |    数据c    |
     * headList.add(Arrays.asList("主标题1","子标题1"));
     * headList.add(Arrays.asList("主标题1","子标题2"));
     * headList.add(Arrays.asList("主标题2","子标题3"));
     * 注意，这里headList每个元素是一列。
     * 如果需要合并的表头，主表头填相同的会自动合并。
     */
    @Deprecated
    public List<HeadData> headList;

    /**
     * 设置列宽
     */
    public Integer columnWidth = 10;

    /**
     * 数据关系映射，未来逐步切换使用该方式，不再通过headList传入
     */
    public String fullClassName;

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getAsync() {
        return isAsync;
    }

    public void setAsync(Boolean async) {
        isAsync = async;
    }

    public Map<String, String> getCustomQueryCondition() {
        return customQueryCondition;
    }

    public void setCustomQueryCondition(Map<String, String> customQueryCondition) {
        this.customQueryCondition = customQueryCondition;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getSheetTotalRows() {
        return sheetTotalRows;
    }

    public void setSheetTotalRows(Integer sheetTotalRows) {
        this.sheetTotalRows = sheetTotalRows;
    }

    public List<HeadData> getHeadList() {
        return headList;
    }

    public void setHeadList(List<HeadData> headList) {
        this.headList = headList;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }
}

