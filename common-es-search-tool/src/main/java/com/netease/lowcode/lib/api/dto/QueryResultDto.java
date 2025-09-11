package com.netease.lowcode.lib.api.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class QueryResultDto {
    public Integer pageSize;
    public Integer pageIndex;
    public Integer totalPage;

    public Long totalCount;
    /**
     * 滚动搜索id
     */
    public String scrollId;

    public List<String> dataJsonString;

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

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public List<String> getDataJsonString() {
        return dataJsonString;
    }

    public void setDataJsonString(List<String> dataJsonString) {
        this.dataJsonString = dataJsonString;
    }
}
