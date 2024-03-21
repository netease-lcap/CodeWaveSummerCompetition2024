package com.netease.lib.generate;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslLogic;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IDUtil {
    @NaslConfiguration
    static
    String randomString = "";
    @NaslConfiguration
    static
    String isDigits = "0123456789";
    @NaslConfiguration
    static
    String isLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 生成随机字符串
     * @param stringLength 生成随机字符串的长度
     * @param includeDigits 是否包含数字
     * @param includeLetters 是否包含字母
     * @return  随机字符串
     */
    @NaslLogic
    public static String generateRandomString(Integer stringLength, Boolean includeDigits, Boolean includeLetters) {
        if (includeDigits == null) {
            includeDigits = false;
        }
        if (includeLetters == null) {
            includeLetters = false;
        }
        if (stringLength == null) {
            return "";
        }
        if (includeDigits) {
            randomString += isDigits;
        }
        if (includeLetters) {
            randomString += isLetters;
        }
        if (randomString.length() == 0) {
            return "";
        }
        SecureRandom random = new SecureRandom();
        String finalRandomString = randomString;
        return IntStream.range(0, stringLength)
                .mapToObj(i -> String.valueOf(finalRandomString.charAt(random.nextInt(finalRandomString.length()))))
                .collect(Collectors.joining());
    }
}
