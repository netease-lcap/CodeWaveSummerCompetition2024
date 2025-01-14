package com.netease.lowcode.lib.officetopdf.util;

import java.net.URLEncoder;

public class UrlEncodeUtil {
    public static String encodeChineseCharacters(String url) {
        StringBuilder encodedUrl = new StringBuilder();
        for (char c : url.toCharArray()) {
            if (isChinese(c)) {
                encodedUrl.append(URLEncoder.encode(String.valueOf(c)));
            } else {
                encodedUrl.append(c);
            }
        }
        return encodedUrl.toString();
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FFF; // 简单判断是否为中文字符，实际中文字符范围更广
    }

}
