package com.netease.lowcode.pdf.extension.spring;

import com.itextpdf.io.font.FontNames;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.netease.lowcode.pdf.extension.utils.FontUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.Set;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Override
    public void run(String... args) throws Exception {
        // 注册系统字体
        logger.info("注册系统字体文件");
        PdfFontFactory.registerSystemDirectories();
        // 获取已注册字体集合
        // 以下会导致oom
        /*Set<String> registeredFonts = PdfFontFactory.getRegisteredFonts();
        if (CollectionUtils.isEmpty(registeredFonts)) {
            return;
        }

        for (String registeredFont : registeredFonts) {
            PdfFont pdfFont = PdfFontFactory.createRegisteredFont(registeredFont);
            if (Objects.isNull(pdfFont)) {
                continue;
            }

            FontNames fontNames = pdfFont.getFontProgram().getFontNames();

            FontCache value = new FontCache();
            value.setFullName(fontNames.getFullName());
            value.setFontName(fontNames.getFontName());
            value.setFamilyName(fontNames.getFamilyName());
            value.setFamilyName2(fontNames.getFamilyName2());

            SoftReference<FontCache> softReference = new SoftReference<>(value);
            FontUtils.registeredFontMap.put(registeredFont, softReference);

            // 小内存应用创建大量字体会导致oom
            // 应用刚启动时多触发几次gc对于普通应用也可以接受,对于高并发应用,启动后建议设置一段流量禁止期+逐步引流
            pdfFont = null;
            System.gc();
        }*/
    }
}
