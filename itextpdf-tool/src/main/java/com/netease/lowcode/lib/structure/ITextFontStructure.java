package com.netease.lowcode.lib.structure;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.springframework.util.StringUtils;

import java.io.IOException;

@NaslStructure
public class ITextFontStructure {
    /**
     * 字体名称，默认STSong-Light
     */
    public String fontName;
    /**
     * 字体编码，默认UniGB-UCS2-H
     */
    public String fontEncoding;
    /**
     * 是否嵌入字体：true表示将字体文件嵌入文档（确保跨设备显示一致），false表示不嵌入
     */
    public Boolean embedded;
    /**
     * 强制读取字体：true表示忽略系统缓存强制重新加载字体文件（解决字体更新问题）
     */
    public Boolean forceRead;
    /**
     * 字体大小（单位：磅/pt），例如 12.0 表示12磅字号
     */
    public Double size;
    /**
     * 样式，枚举：NORMAL/BOLD/ITALIC/UNDERLINE/STRIKETHRU/BOLDITALIC/UNDEFINED/DEFAULTSIZE
     */
    public String style;
    /**
     * 颜色，枚举：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE
     */
    public String color;

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontEncoding() {
        return fontEncoding;
    }

    public void setFontEncoding(String fontEncoding) {
        this.fontEncoding = fontEncoding;
    }

    public Boolean getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Boolean embedded) {
        this.embedded = embedded;
    }

    public Boolean getForceRead() {
        return forceRead;
    }

    public void setForceRead(Boolean forceRead) {
        this.forceRead = forceRead;
    }


    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String  getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
