package com.netease.lib.redistemplatetool.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

/**
 * 应用启动时权限数据上报任务类
 *
 * @author sys
 * @since 3.9
 */
public class LettuceVersionCheckUtil {

    private static final String minVersion = "2.4.1";
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    public static int compareVersionsWithSuffix(String v1, String v2) {
        // 移除后缀（保留数字部分）
        String cleanV1 = v1.split("-")[0].replaceAll("[^0-9.]", "");
        String cleanV2 = v2.split("-")[0].replaceAll("[^0-9.]", "");

        return compareVersions(cleanV1, cleanV2);
    }

    public static int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int num1 = (i < parts1.length) ?
                    Integer.parseInt(parts1[i]) : 0;
            int num2 = (i < parts2.length) ?
                    Integer.parseInt(parts2[i]) : 0;

            if (num1 != num2) {
                return num1 - num2;
            }
        }
        return 0;
    }

    public static void lettuceVersionCheckTask() {
        Package lettucePackage = LettuceClientConfiguration.class.getPackage();
        String lettucePackageVersion = lettucePackage.getImplementationVersion();
        int result = compareVersionsWithSuffix(lettucePackageVersion, minVersion);
        if (result < 0) {
            log.info("spring-data-redis版本 {} 低于要求版本 {}", lettucePackageVersion, minVersion);
            throw new RuntimeException("spring-data-redis版本低于要求版本，须按文档要求配套使用翻译器插件");
        }
    }


}
