package com.netease.lowcode.lib.enums;

import com.itextpdf.text.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RectangleEnum {
    UNDEFINED("UNDEFINED", Rectangle.UNDEFINED),
    TOP("TOP", Rectangle.TOP),
    BOTTOM("BOTTOM", Rectangle.BOTTOM),
    LEFT("LEFT", Rectangle.LEFT),
    RIGHT("RIGHT", Rectangle.RIGHT),
    NO_BORDER("NO_BORDER", Rectangle.NO_BORDER),
    BOX("BOX", Rectangle.BOX);;
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private String rectangleText;
    private Integer rectangle;

    RectangleEnum(String rectangleText, Integer rectangle) {
        this.rectangleText = rectangleText;
        this.rectangle = rectangle;
    }

    /**
     * 根据rectangleText获取rectangle
     *
     * @return rectangle
     */
    public static Integer getRectangle(String rectangleText) {
        for (RectangleEnum rectangleEnum : RectangleEnum.values()) {
            if (rectangleEnum.getRectangleText().equals(rectangleText)) {
                return rectangleEnum.getRectangle();
            }
        }
        log.error("未定义的布局rectangleText:{}", rectangleText);
        return Rectangle.UNDEFINED;
    }

    public String getRectangleText() {
        return rectangleText;
    }

    public void setRectangleText(String rectangleText) {
        this.rectangleText = rectangleText;
    }

    public Integer getRectangle() {
        return rectangle;
    }

    public void setRectangle(Integer rectangle) {
        this.rectangle = rectangle;
    }
}
