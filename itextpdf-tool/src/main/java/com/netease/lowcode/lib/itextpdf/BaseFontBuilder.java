package com.netease.lowcode.lib.itextpdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.netease.lowcode.lib.enums.BaseColorEnum;
import com.netease.lowcode.lib.enums.FontStyleEnum;
import com.netease.lowcode.lib.structure.ITextFontStructure;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class BaseFontBuilder {
    public static Font setFont(ITextFontStructure iTextFontStructure) throws DocumentException, IOException {
        if (StringUtils.isEmpty(iTextFontStructure.getFontEncoding())) {
            iTextFontStructure.setFontEncoding("UniGB-UCS2-H");
        }
        if (iTextFontStructure.getEmbedded() == null) {
            iTextFontStructure.setEmbedded(false);
        }
        if (iTextFontStructure.getForceRead() == null) {
            iTextFontStructure.setForceRead(false);
        }
        if (iTextFontStructure.getSize() == null) {
            iTextFontStructure.setSize(-1.0d);
        }
        if (StringUtils.isEmpty(iTextFontStructure.getFontName())) {
            iTextFontStructure.setFontName("STSong-Light");
        }
        BaseFont baseFont = BaseFont.createFont(iTextFontStructure.getFontName(), iTextFontStructure.getFontEncoding(), iTextFontStructure.getEmbedded(), iTextFontStructure.getForceRead());
        Integer style = FontStyleEnum.getFontStyle(iTextFontStructure.getStyle());
        return new Font(baseFont, Float.parseFloat(iTextFontStructure.getSize() + ""), style, BaseColorEnum.getBaseColor(iTextFontStructure.getColor()));
    }

}
