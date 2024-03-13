package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.util.Random;

public class IDUtil {
    @NaslLogic
    public static String generateRandomString(Integer stringLength, Boolean includeDigits, Boolean includeLetters) {
        String randomString = "";

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
            randomString += "0123456789";
        }
        if (includeLetters) {
            randomString += "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (randomString.length() == 0) {
            return "";
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            sb.append(randomString.charAt(random.nextInt(randomString.length())));
        }
        return sb.toString();
    }
}
