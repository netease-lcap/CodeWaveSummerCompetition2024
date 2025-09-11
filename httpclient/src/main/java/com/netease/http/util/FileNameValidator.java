package com.netease.http.util;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class FileNameValidator {

    // 非法字符正则（Windows+Linux）
    private static final Pattern ILLEGAL_CHARS = Pattern.compile(
            "[\\\\/:*?\"<>|\u0000-\u001F]|[\uD800-\uDFFF]"
    );

    // Windows保留文件名（不区分大小写）
    private static final String[] WINDOWS_RESERVED_NAMES = {
            "CON", "PRN", "AUX", "NUL",
            "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
            "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
    };

    /**
     * 校验文件名合法性
     *
     * @param filename  待校验字符串
     * @param maxLength 最大允许长度（建议255）
     * @return 校验结果（true=合法）
     */
    public static boolean isValidFilename(String filename, int maxLength) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        // 1. 长度检查
        if (maxLength != 0 && filename.length() > maxLength) {
            return false;
        }

        // 2. 非法字符检查
        if (ILLEGAL_CHARS.matcher(filename).find()) {
            return false;
        }

        // 3. 保留名称检查（Windows）
        if (isWindowsReservedName(filename)) {
            return false;
        }

        // 4. 路径有效性检查
        try {
            Paths.get(filename);
        } catch (InvalidPathException e) {
            return false;
        }

        return true;
    }

    private static boolean isWindowsReservedName(String filename) {
        String nameWithoutExt = filename.split("\\.")[0].toUpperCase();
        for (String reserved : WINDOWS_RESERVED_NAMES) {
            if (reserved.equals(nameWithoutExt)) {
                return true;
            }
        }
        return false;
    }
}