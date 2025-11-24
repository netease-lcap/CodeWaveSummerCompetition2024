package com.netease.lowcode.lib.enums;

import com.itextpdf.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FontStyleEnum {
    NORMAL("NORMAL", Font.NORMAL),
    BOLD("BOLD", Font.BOLD),
    ITALIC("ITALIC", Font.ITALIC),
    UNDERLINE("UNDERLINE", Font.UNDERLINE),
    STRIKETHRU("STRIKETHRU", Font.STRIKETHRU),
    BOLDITALIC("BOLDITALIC", Font.BOLDITALIC),
    UNDEFINED("UNDEFINED", Font.UNDEFINED),
    DEFAULTSIZE("DEFAULTSIZE", Font.DEFAULTSIZE),
    ;
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private String style;
    private Integer fontStyle;

    FontStyleEnum(String style, Integer fontStyle) {
        this.style = style;
        this.fontStyle = fontStyle;
    }

    /**
     * 根据style查询fontStyle
     *
     * @param style
     */
    public static Integer getFontStyle(String style) {
        for (FontStyleEnum fontStyleEnum : FontStyleEnum.values()) {
            if (fontStyleEnum.getStyle().equals(style)) {
                return fontStyleEnum.getFontStyle();
            }
        }
        log.error("未定义的字体style:{}", style);
        return Font.NORMAL;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(Integer fontStyle) {
        this.fontStyle = fontStyle;
    }
}
