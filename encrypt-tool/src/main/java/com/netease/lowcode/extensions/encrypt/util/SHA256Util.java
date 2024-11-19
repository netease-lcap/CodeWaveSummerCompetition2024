package com.netease.lowcode.extensions.encrypt.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256Util {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private SHA256Util() {

    }

    /**
     * SHA-256加密
     *
     * @param sourceString 待加密字符串
     * @return 编码后的字节数组
     */
    public static byte[] encrypt(String sourceString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(sourceString.getBytes(DEFAULT_CHARSET));
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
