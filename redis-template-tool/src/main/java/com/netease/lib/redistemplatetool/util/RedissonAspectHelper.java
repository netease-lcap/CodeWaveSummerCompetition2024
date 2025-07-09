package com.netease.lib.redistemplatetool.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.annotation.context.LogicContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * redis帮助类
 */
public class RedissonAspectHelper {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    public static boolean isLockAnnotation(String logicName) {
        return listJobContext().stream().anyMatch(e -> e.getLogicName().equals(logicName));
    }

    public static List<LogicContext> listJobContext() {
        String filePath = "annotation/annotation_metadata_logic_RedissonLogicAnnotation.json";
        return readFileToCollect(filePath, new TypeReference<List<LogicContext>>() {
        });
    }

    /**
     * 从主程序源json文件中读取配置数据
     */
    private static <T> T readFileToCollect(String filePath, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
        } catch (FileNotFoundException fnfe) {
            log.info("library file [{}] not exist", filePath);
            return null;
        } catch (IOException e) {
            log.error("library fail to read file [{}]", filePath, e);
            return null;
        }
        T readValue = null;
        try {
            readValue = objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            log.error("library fail to deserialize file content [{}]", filePath, e);
            return null;
        }
        return readValue;
    }
}
