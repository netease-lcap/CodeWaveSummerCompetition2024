package com.netease.http.util;

public class FileNameValidator {
    private static final String FILE_NAME_REGEX = "^[^\\\\/:*?\"<>|]*$"; // Windows文件名规则

    public static boolean isValidFileName(String fileName) {
        return fileName.matches(FILE_NAME_REGEX);
    }
}