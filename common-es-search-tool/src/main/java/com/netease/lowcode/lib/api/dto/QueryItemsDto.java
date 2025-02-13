package com.netease.lowcode.lib.api.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class QueryItemsDto {
    /**
     * 字段名称
     */
    public String parameter;
    /**
     * 查询类型，精确1、模糊2、范围3、不等于4
     */
    public Integer queryType;
    /**
     * 查询内容：文本、范围-范围格式为 a,b。大于等于a，小于等于b。如只有大于，则为 a，如只有小于，则为 ,b
     */
    public String queryValue;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }
}
