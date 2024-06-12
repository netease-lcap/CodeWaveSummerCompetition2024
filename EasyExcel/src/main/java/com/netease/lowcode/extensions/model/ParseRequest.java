package com.netease.lowcode.extensions.model;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ParseRequest {

    /**
     * excel的url
     */
    public String url;

    /**
     * 是否包含图片列需要解析，默认为false
     */
    public Boolean hasImageColumn = false;

    /**
     * 解析结构类的全路径名
     */
    public String fullClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public Boolean getHasImageColumn() {
        return hasImageColumn;
    }

    public void setHasImageColumn(Boolean hasImageColumn) {
        this.hasImageColumn = hasImageColumn;
    }
}
