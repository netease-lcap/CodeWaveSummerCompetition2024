package com.netease.lowcode.extensions.easyexcel.poi;

public class CellData {

    private Integer index;

    /**
     * 单元格数据
     */
    private Object data;

    /**
     * 单元格类型枚举
     */

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * 单元格样式信息
     *
     */
    private CellStyle cellStyle;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
}
