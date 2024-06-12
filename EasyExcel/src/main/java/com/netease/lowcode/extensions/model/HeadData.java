package com.netease.lowcode.extensions.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class HeadData {

    /**
     * 这代表一列标题
     */
    public List<String> titles;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}