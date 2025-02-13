package com.netease.lowcode.lib.api.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class QueryItemsListDto {
    /**
     * 字段查询条件列表，可空
     */
    public List<QueryItemsDto> queryItems;

    public List<QueryItemsDto> getQueryItems() {
        return queryItems;
    }

    public void setQueryItems(List<QueryItemsDto> queryItems) {
        this.queryItems = queryItems;
    }
}
