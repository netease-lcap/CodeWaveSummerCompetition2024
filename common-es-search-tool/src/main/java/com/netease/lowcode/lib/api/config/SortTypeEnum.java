package com.netease.lowcode.lib.api.config;

import org.elasticsearch.search.sort.SortOrder;

public enum SortTypeEnum {
    SortTypeAsc(SortOrder.ASC, 1),
    SortTypeDesc(SortOrder.DESC, 2);

    private Integer value;
    private SortOrder sortOrder;

    SortTypeEnum(SortOrder sortOrder, int i) {
        this.sortOrder = sortOrder;
        this.value = i;
    }

    /**
     * 根据value获取对应的枚举
     */
    public static SortOrder getEnumByValue(Integer value) {
        for (SortTypeEnum sortTypeEnum : SortTypeEnum.values()) {
            if (sortTypeEnum.value.equals(value)) {
                return sortTypeEnum.sortOrder;
            }
        }
        return null;
    }
}
