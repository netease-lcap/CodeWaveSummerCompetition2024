package com.netease.lowcode.lib.api.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class SortInfoDto {
    /**
     * 排序类型，升序1、降序2
     */
    public Integer sortType;
    /**
     * 排序字段名称
     */
    public String sortParam;

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getSortParam() {
        return sortParam;
    }

    public void setSortParam(String sortParam) {
        this.sortParam = sortParam;
    }
}
