package com.netease.lowcode.font.api;


import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


@Component
public class FontInitService {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Value("${fontFileDir}")
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "/usr/share/fonts/truetype/dejavu"),
            @Environment(type = EnvironmentType.ONLINE, value = "/usr/share/fonts/truetype/dejavu")})
    public String fontFileDir;

    @NaslLogic
    public static Boolean test() {
        return true;
    }

    @PostConstruct
    public void init() {
        try {
            File targetDir = new File(fontFileDir);
            if (!targetDir.exists()) {
                // 初始化时创建字体文件夹目录，读取resources目录下的字体文件
                targetDir.mkdirs();
            }
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(FontInitService.class.getClassLoader());
            Resource[] fonts = patternResolver.getResources("classpath:fonts/*");
            log.info("fonts 文件数:{}", fonts.length);
            for (Resource font : fonts) {
                File destFile = new File(targetDir, font.getFilename());
                if (!destFile.exists()) {
                    try (InputStream inputStream = patternResolver.getResource("fonts/" + font.getFilename()).getInputStream()) {
                        Files.copy(inputStream, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        log.info("新增字体文件{}，{}成功", font.getFilename(), destFile.toPath());
                    }
                } else {
                    log.info("字体文件{}，{}已存在", font.getFilename(), destFile.toPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
