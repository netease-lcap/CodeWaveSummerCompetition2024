package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class ITextTableStructure {
    /**
     * 表格宽度占页面宽度的百分比（如 100%）
     */
    public Double widthPercentage;
    /**
     * 表格绝对宽度（单位：像素）
     */
    public Double totalWidth;
    /**
     * 是否固定表格宽度（true 时忽略自动调整）
     */
    public Boolean lockedWidth;
    /**
     * 水平对齐方式（ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL）
     */
    public String horizontalAlignmentText;
    /**
     * 表格前的空白间距
     */
    public Double spacingBefore;
    /**
     * 表格后的空白间距
     */
    public Double spacingAfter;
    /**
     * 表格列数
     */
    public Integer numColumns;
    /**
     * 每列宽度比例，数组大小与numColumns一致（如 new Double[]{1, 2, 3} 表示三列比例为1:2:3）
     */
    public List<Double> widths;
    /**
     * 指定前N行为表头（跨页时自动重复）
     */
    public Integer headerRows;
    /**
     * 指定后N行为表尾（跨页时自动重复）
     */
    public Integer footerRows;

    /**
     * 是否允许拆分单元格（false 时强制整单元格换页）
     */
    public Boolean splitLate;
    /**
     * 是否跳过第一页的表头（用于连续表格）
     */
    public Boolean skipFirstHeader;

    public Integer getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(Integer numColumns) {
        this.numColumns = numColumns;
    }

    public Double getWidthPercentage() {
        return widthPercentage;
    }

    public void setWidthPercentage(Double widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    public Double getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(Double totalWidth) {
        this.totalWidth = totalWidth;
    }

    public Boolean getLockedWidth() {
        return lockedWidth;
    }

    public void setLockedWidth(Boolean lockedWidth) {
        this.lockedWidth = lockedWidth;
    }

    public String getHorizontalAlignmentText() {
        return horizontalAlignmentText;
    }

    public void setHorizontalAlignmentText(String horizontalAlignmentText) {
        this.horizontalAlignmentText = horizontalAlignmentText;
    }

    public Double getSpacingBefore() {
        return spacingBefore;
    }

    public void setSpacingBefore(Double spacingBefore) {
        this.spacingBefore = spacingBefore;
    }

    public Double getSpacingAfter() {
        return spacingAfter;
    }

    public void setSpacingAfter(Double spacingAfter) {
        this.spacingAfter = spacingAfter;
    }

    public List<Double> getWidths() {
        return widths;
    }

    public void setWidths(List<Double> widths) {
        this.widths = widths;
    }

    public Integer getHeaderRows() {
        return headerRows;
    }

    public void setHeaderRows(Integer headerRows) {
        this.headerRows = headerRows;
    }

    public Integer getFooterRows() {
        return footerRows;
    }

    public void setFooterRows(Integer footerRows) {
        this.footerRows = footerRows;
    }

    public Boolean getSplitLate() {
        return splitLate;
    }

    public void setSplitLate(Boolean splitLate) {
        this.splitLate = splitLate;
    }

    public Boolean getSkipFirstHeader() {
        return skipFirstHeader;
    }

    public void setSkipFirstHeader(Boolean skipFirstHeader) {
        this.skipFirstHeader = skipFirstHeader;
    }
}
