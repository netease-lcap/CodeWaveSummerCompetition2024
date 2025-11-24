package com.netease.lowcode.lib.enums;

import com.itextpdf.text.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ElementAlignEnum {
    ALIGN_UNDEFINED("ALIGN_UNDEFINED", Element.ALIGN_UNDEFINED),
    ALIGN_LEFT("ALIGN_LEFT", Element.ALIGN_LEFT),
    ALIGN_CENTER("ALIGN_CENTER", Element.ALIGN_CENTER),
    ALIGN_RIGHT("ALIGN_RIGHT", Element.ALIGN_RIGHT),
    ALIGN_JUSTIFIED("ALIGN_JUSTIFIED", Element.ALIGN_JUSTIFIED),
    ALIGN_TOP("ALIGN_TOP", Element.ALIGN_TOP),
    ALIGN_MIDDLE("ALIGN_MIDDLE", Element.ALIGN_MIDDLE),
    ALIGN_BOTTOM("ALIGN_BOTTOM", Element.ALIGN_BOTTOM),
    ALIGN_BASELINE("ALIGN_BASELINE", Element.ALIGN_BASELINE),
    ALIGN_JUSTIFIED_ALL("ALIGN_JUSTIFIED_ALL", Element.ALIGN_JUSTIFIED_ALL),
    ;
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private String alignText;
    private Integer alignElement;

    ElementAlignEnum(String alignText, Integer alignElement) {
        this.alignText = alignText;
        this.alignElement = alignElement;
    }

    public static Integer getAlignElement(String alignText) {
        for (ElementAlignEnum elementAlignEnum : ElementAlignEnum.values()) {
            if (elementAlignEnum.alignText.equals(alignText)) {
                return elementAlignEnum.alignElement;
            }
        }
        log.error("未定义的样式alignText:{}", alignText);
        return Element.ALIGN_UNDEFINED;
    }

}
