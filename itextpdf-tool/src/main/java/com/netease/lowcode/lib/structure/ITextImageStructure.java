package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class ITextImageStructure {
    /**
     * 按比例缩放图片到目标尺寸（保持宽高比）
     */
    public Double scaleToFitHeight;
    /**
     * 按比例缩放图片到目标尺寸（保持宽高比）
     */
    public Double scaleToFitWidth;
    /**
     * 缩放图片的百分比
     */
    public Double scalePercent;
    /**
     * 固定宽度，高度按比例调整。
     */
    public Double scaleAbsoluteWidth;
    /**
     * 固定高度，宽度按比例调整。
     */
    public Double scaleAbsoluteHeight;
    /**
     * 对齐方式
     * ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL
     */
    public String alignText;
    /**
     * 设置绝对坐标（相对于页面左下角）
     */
    public Double absoluteX;
    /**
     * 设置绝对坐标（相对于页面左下角）
     */
    public Double absoluteY;
    /**
     * 左缩进（需在非绝对定位模式下使用）
     */
    public Double indentationLeft;
    /**
     * 全局边框（UNDEFINED/TOP/BOTTOM/LEFT/RIGHT/NO_BORDER/BOX）
     */
    public String border;
    /**
     * 边框宽度
     */
    public Double borderWidth;

    /**
     * 边框颜色，枚举：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE
     */
    public String borderColor;
    /**
     * 图片上方的空白间距
     */
    public Double spacingBefore;
    /**
     * 图片下方的空白间距
     */
    public Double spacingAfter;
    /**
     * 置透明度（ARGB值），示例：{200, 255, 255, 255}
     */
    public List<Integer> transparency;
    /**
     * 设置超链接（点击跳转URL）
     */
    public String anchor;
    /**
     * 设置JPEG压缩质量（0-9，0=最低压缩）
     */
    public Integer compressionLevel;
    /**
     * 设置DPI（影响打印质量）
     */
    public Integer dpiX;
    /**
     * 设置DPI（影响打印质量）
     */
    public Integer dpiY;

    public Double getScaleToFitHeight() {
        return scaleToFitHeight;
    }

    public void setScaleToFitHeight(Double scaleToFitHeight) {
        this.scaleToFitHeight = scaleToFitHeight;
    }

    public Double getScaleToFitWidth() {
        return scaleToFitWidth;
    }

    public void setScaleToFitWidth(Double scaleToFitWidth) {
        this.scaleToFitWidth = scaleToFitWidth;
    }

    public Double getScalePercent() {
        return scalePercent;
    }

    public void setScalePercent(Double scalePercent) {
        this.scalePercent = scalePercent;
    }

    public Double getScaleAbsoluteWidth() {
        return scaleAbsoluteWidth;
    }

    public void setScaleAbsoluteWidth(Double scaleAbsoluteWidth) {
        this.scaleAbsoluteWidth = scaleAbsoluteWidth;
    }

    public Double getScaleAbsoluteHeight() {
        return scaleAbsoluteHeight;
    }

    public void setScaleAbsoluteHeight(Double scaleAbsoluteHeight) {
        this.scaleAbsoluteHeight = scaleAbsoluteHeight;
    }

    public String getAlignText() {
        return alignText;
    }

    public void setAlignText(String alignText) {
        this.alignText = alignText;
    }

    public Double getAbsoluteX() {
        return absoluteX;
    }

    public void setAbsoluteX(Double absoluteX) {
        this.absoluteX = absoluteX;
    }

    public Double getAbsoluteY() {
        return absoluteY;
    }

    public void setAbsoluteY(Double absoluteY) {
        this.absoluteY = absoluteY;
    }

    public Double getIndentationLeft() {
        return indentationLeft;
    }

    public void setIndentationLeft(Double indentationLeft) {
        this.indentationLeft = indentationLeft;
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

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
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

    public List<Integer> getTransparency() {
        return transparency;
    }

    public void setTransparency(List<Integer> transparency) {
        this.transparency = transparency;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Integer getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(Integer compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public Integer getDpiX() {
        return dpiX;
    }

    public void setDpiX(Integer dpiX) {
        this.dpiX = dpiX;
    }

    public Integer getDpiY() {
        return dpiY;
    }

    public void setDpiY(Integer dpiY) {
        this.dpiY = dpiY;
    }
}
