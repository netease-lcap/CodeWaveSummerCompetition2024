package com.netease.lowcode.extensions.easyexcel.poi;

import org.apache.poi.ss.usermodel.IndexedColors;

public class CellStyle {

    // 单元格背景填充颜色
    private String background;
    // 根据条件设置背景填充颜色
    // eg. GREEN<20:RED<BLACK，等于20为红色，小于为绿色、大于为黑色
    private String backgroundCondition;
    // 根据单元格条件设置整行背景颜色
    // eg. 同 @backgroundCondition
    private String rowBackgroundCondition;
    // 列表头
    private String title;
    // 指定列的顺序
    private Integer index;

    // 列宽，单位为一个字符的宽度
    private Integer colWidth;

    public String getBackgroundStr(){
        return this.background;
    }

    public short getBackground() {
        return IndexedColors.valueOf(this.background).getIndex();
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getBackgroundCondition() {
        return backgroundCondition;
    }

    public void setBackgroundCondition(String backgroundCondition) {
        this.backgroundCondition = backgroundCondition;
    }

    public Integer getColWidth() {
        return colWidth;
    }

    public void setColWidth(Integer colWidth) {
        this.colWidth = colWidth;
    }

    public String getRowBackgroundCondition() {
        return rowBackgroundCondition;
    }

    public void setRowBackgroundCondition(String rowBackgroundCondition) {
        this.rowBackgroundCondition = rowBackgroundCondition;
    }

    public CellStyle clone() {
        CellStyle cellStyle = new CellStyle();
        cellStyle.setBackground(this.background);
        cellStyle.setTitle(this.title);
        cellStyle.setIndex(this.index);
        cellStyle.setBackgroundCondition(this.backgroundCondition);
        cellStyle.setColWidth(this.colWidth);
        cellStyle.setRowBackgroundCondition(this.rowBackgroundCondition);
        return cellStyle;
    }
}
