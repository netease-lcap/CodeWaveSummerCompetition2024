package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ITextCellStructure {
    /**
     * 全局边框（UNDEFINED/TOP/BOTTOM/LEFT/RIGHT/NO_BORDER/BOX）
     */
    public String border;
    /**
     * 单元格边框宽度
     */
    public Double borderWidth;
    /**
     * 单元格内边距
     */
    public Double padding;
    /**
     * 边框颜色，枚举：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE
     */
    public String borderColor;
    /**
     * 左边距
     */
    public Double paddingLeft;
    /**
     * 右边距
     */
    public Double paddingRight;
    /**
     * 上边距
     */
    public Double paddingTop;
    /**
     * 下边距
     */
    public Double paddingBottom;
    /**
     * 水平对齐方式（ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL）
     */
    public String horizontalAlignmentText;
    /**
     * 垂直对齐方式（ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL）
     */
    public String verticalAlignmentText;
    /**
     * 固定高度
     */
    public Double fixedHeight;
    /**
     * 最小高度
     */
    public Double minimumHeight;
    /**
     * 行间距（固定值）
     */
    public Double fixedLeading;
    /**
     * 行间距（倍数）
     */
    public Double multipliedLeading;
    /**
     * 背景颜色，枚举：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE
     */
    public String backgroundColor;
    /**
     * 横向合并列数
     */
    public Integer colSpan;
    /**
     * 纵向合并行数
     */
    public Integer rowSpan;

    public String getVerticalAlignmentText() {
        return verticalAlignmentText;
    }

    public void setVerticalAlignmentText(String verticalAlignmentText) {
        this.verticalAlignmentText = verticalAlignmentText;
    }

    public String getHorizontalAlignmentText() {
        return horizontalAlignmentText;
    }

    public void setHorizontalAlignmentText(String horizontalAlignmentText) {
        this.horizontalAlignmentText = horizontalAlignmentText;
    }


    public Double getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(Double fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public Double getMinimumHeight() {
        return minimumHeight;
    }

    public void setMinimumHeight(Double minimumHeight) {
        this.minimumHeight = minimumHeight;
    }

    public Double getFixedLeading() {
        return fixedLeading;
    }

    public void setFixedLeading(Double fixedLeading) {
        this.fixedLeading = fixedLeading;
    }

    public Double getMultipliedLeading() {
        return multipliedLeading;
    }

    public void setMultipliedLeading(Double multipliedLeading) {
        this.multipliedLeading = multipliedLeading;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public Double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Double getPadding() {
        return padding;
    }

    public void setPadding(Double padding) {
        this.padding = padding;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Double getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(Double paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public Double getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(Double paddingRight) {
        this.paddingRight = paddingRight;
    }

    public Double getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(Double paddingTop) {
        this.paddingTop = paddingTop;
    }

    public Double getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(Double paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
}
