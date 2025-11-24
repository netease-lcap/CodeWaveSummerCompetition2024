package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ITextParagraphStructure {
    /**
     * 对齐方式
     * ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL
     */
    public String alignText;
    /**
     * 左缩进（单位：像素）
     */
    public Double indentationLeft;

    /**
     * 右缩进（单位：像素）
     */
    public Double indentationRight;

    /**
     * 首行缩进
     */
    public Double firstLineIndent;

    /**
     * 段前间距
     */
    public Double spacingBefore;

    /**
     * 段后间距
     */
    public Double spacingAfter;
    /**
     * 是否禁止分页时拆分段落（true 表示整段必须在同一页）]
     */
    public Boolean keepTogether;
    /**
     * 行间距（固定值）
     */
    public Double fixedLeading;
    /**
     * 行间距（倍数）
     */
    public Double multipliedLeading;

    public String getAlignText() {
        return alignText;
    }

    public void setAlignText(String alignText) {
        this.alignText = alignText;
    }

    public Double getIndentationLeft() {
        return indentationLeft;
    }

    public void setIndentationLeft(Double indentationLeft) {
        this.indentationLeft = indentationLeft;
    }

    public Double getIndentationRight() {
        return indentationRight;
    }

    public void setIndentationRight(Double indentationRight) {
        this.indentationRight = indentationRight;
    }

    public Double getFirstLineIndent() {
        return firstLineIndent;
    }

    public void setFirstLineIndent(Double firstLineIndent) {
        this.firstLineIndent = firstLineIndent;
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

    public Boolean getKeepTogether() {
        return keepTogether;
    }

    public void setKeepTogether(Boolean keepTogether) {
        this.keepTogether = keepTogether;
    }
}
