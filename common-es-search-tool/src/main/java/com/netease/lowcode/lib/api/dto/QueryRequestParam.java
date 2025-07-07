package com.netease.lowcode.lib.api.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class QueryRequestParam {
    /**
     * 每页大小，可空，默认20
     */
    public Integer pageSize;
    /**
     * 页码，可空，默认1
     */
    public Integer pageIndex;
    /**
     * 滚动搜索有效时间段，不传即不使用滚动搜索
     */
    public Integer scrollTime;
    /**
     * 索引名称，必填
     */
    public String index;
    /**
     * 排序信息，可空
     */
    public SortInfoDto sortInfo;
    /**
     * queryItems内多个条件的关系。1-与，2-或。可空，默认1
     */
    public Integer logicalOperator;
    /**
     * 字段查询条件列表，可空（与queryItemList二选一）
     * logicalOperator=1表示queryItems内多个条件之间为与；logicalOperator=2表示queryItems内多个条件之间为或。
     */
    public List<QueryItemsDto> queryItems;
    /**
     * 字段查询条件列表，可空（与queryItems二选一）
     * logicalOperator=1表示queryItemList这一层多个对象之间为与，QueryItemsListDto第二层中多个对象之间为或；
     * logicalOperator=2表示queryItemList这一层多个对象之间为或，QueryItemsListDto第二层中多个对象之间为与。
     */
    public List<QueryItemsListDto> queryItemList;
    /**
     * 需要返回的字段列表，可空，默认返回全部
     */
    public List<String> includeFields;
    /**
     * 需要从返回结果中排除的字段列表，可空，默认不排除
     */
    public List<String> excludeFields;

    public List<QueryItemsDto> getQueryItems() {
        return queryItems;
    }

    public void setQueryItems(List<QueryItemsDto> queryItems) {
        this.queryItems = queryItems;
    }

    public Integer getScrollTime() {
        return scrollTime;
    }

    public void setScrollTime(Integer scrollTime) {
        this.scrollTime = scrollTime;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public void setIncludeFields(List<String> includeFields) {
        this.includeFields = includeFields;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public SortInfoDto getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfoDto sortInfo) {
        this.sortInfo = sortInfo;
    }

    public Integer getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(Integer logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public List<QueryItemsListDto> getQueryItemList() {
        return queryItemList;
    }

    public void setQueryItemList(List<QueryItemsListDto> queryItemList) {
        this.queryItemList = queryItemList;
    }
}
