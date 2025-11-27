package com.netease.lowcode.lib.enums;

import com.itextpdf.text.BaseColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BaseColorEnum {
    WHITE("WHITE", BaseColor.WHITE),
    LIGHT_GRAY("LIGHT_GRAY", BaseColor.LIGHT_GRAY),
    GRAY("GRAY", BaseColor.GRAY),
    DARK_GRAY("DARK_GRAY", BaseColor.DARK_GRAY),
    BLACK("BLACK", BaseColor.BLACK),
    RED("RED", BaseColor.RED),
    PINK("PINK", BaseColor.PINK),
    ORANGE("ORANGE", BaseColor.ORANGE),
    YELLOW("YELLOW", BaseColor.YELLOW),
    GREEN("GREEN", BaseColor.GREEN),
    MAGENTA("MAGENTA", BaseColor.MAGENTA),
    CYAN("CYAN", BaseColor.CYAN),
    BLUE("BLUE", BaseColor.BLUE),
    ;
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private String color;
    private BaseColor baseColor;

    BaseColorEnum(String color, BaseColor baseColor) {
        this.color = color;
        this.baseColor = baseColor;
    }

    /**
     * 根据color获取BaseColor
     */
    public static BaseColor getBaseColor(String color) {
        for (BaseColorEnum value : BaseColorEnum.values()) {
            if (value.color.equals(color)) {
                return value.baseColor;
            }
        }
        log.error("未定义的颜色color:{}", color);
        return BaseColor.BLACK;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BaseColor getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(BaseColor baseColor) {
        this.baseColor = baseColor;
    }
}
