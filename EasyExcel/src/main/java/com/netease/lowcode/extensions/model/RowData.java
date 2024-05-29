package com.netease.lowcode.extensions.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class RowData {

    /**
     * 每行的数据
     */
    public List<String> columns;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
