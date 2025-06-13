package com.netease.lowcode.pdf.extension.utils;

import com.itextpdf.io.font.FontNames;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.pdf.extension.spring.FontCache;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FontUtils {

    // 将所有字体创建出来 可能造成 oom ,因此仅标记字体
    public static Map<String, SoftReference<FontCache>> registeredFontMap = new ConcurrentHashMap<>();
    // 不同pdf文档无法共用字体对象 Pdf indirect object belongs to other PDF document. Copy object to current pdf document
    public static Map<String,SoftReference<PdfFont>> fontCacheMap = new ConcurrentHashMap<>();

    public static PdfFont createDefaultFont() {
        try {
            return PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PdfFont createFont(String font) {
        if (StringUtils.isBlank(font)) {
            return createDefaultFont();
        }

        try {

            // 解析excel获取的font信息可能与注册的名称不匹配，这里不做处理了
            PdfFont registeredFont = PdfFontFactory.createRegisteredFont(font);
            if (Objects.nonNull(registeredFont)) {
                return registeredFont;
            }
        } catch (IOException e) {
            // 不做处理
        }

        // 以下会导致oom
        /*for (Map.Entry<String, SoftReference<FontCache>> entry : registeredFontMap.entrySet()) {
            FontCache fontCache = entry.getValue().get();
            // 已被回收
            if (Objects.isNull(fontCache)) {
                PdfFont pdfFont = null;
                try {
                    pdfFont = PdfFontFactory.createRegisteredFont(entry.getKey());
                } catch (IOException e) {
                    // do nothing
                }
                if (Objects.isNull(pdfFont)) {
                    continue;
                }
                FontNames fontNames = pdfFont.getFontProgram().getFontNames();
                fontCache = new FontCache();
                fontCache.setFullName(fontNames.getFullName());
                fontCache.setFontName(fontNames.getFontName());
                fontCache.setFamilyName(fontNames.getFamilyName());
                fontCache.setFamilyName2(fontNames.getFamilyName2());
                entry.setValue(new SoftReference<>(fontCache));
                pdfFont = null;
            }
            String[][] fullName = fontCache.getFullName();
            String fontName = fontCache.getFontName();
            String[][] familyName = fontCache.getFamilyName();
            String[][] familyName2 = fontCache.getFamilyName2();
            if (compareName(fullName, font) || StringUtils.equalsIgnoreCase(fontName, font) || compareName(familyName, font) || compareName(familyName2, font)) {
                try {
                    return PdfFontFactory.createRegisteredFont(entry.getKey());
                } catch (IOException e) {
                    // do nothing
                    break;
                }
            }
        }*/

        return createDefaultFont();
    }

    private static boolean compareName(String[][] names, String font) {
        if (Objects.isNull(names) || StringUtils.isBlank(font)) {
            return false;
        }
        for (String[] name : names) {
            if (Objects.isNull(name)) {
                continue;
            }
            for (String s : name) {
                if (StringUtils.equalsIgnoreCase(s, font)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 查看系统字体family
     *
     * @return
     */
    @NaslLogic
    public static List<String> getAvailableFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilyNames = ge.getAvailableFontFamilyNames();
        return Arrays.asList(fontFamilyNames);
    }

    /**
     * 查看已注册的字体
     *
     * @return
     */
    @NaslLogic
    public static List<String> getRegisteredFonts() {
        return Arrays.asList(PdfFontFactory.getRegisteredFonts().toArray(new String[0]));
    }

}
