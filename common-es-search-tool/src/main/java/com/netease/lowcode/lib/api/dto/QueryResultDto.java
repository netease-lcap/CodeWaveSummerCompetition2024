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
}
