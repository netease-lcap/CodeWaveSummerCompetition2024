package com.netease.lowcode.extensions.encrypt.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * SHA1加密
 */
public class SHA1Util {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private SHA1Util() {

    }

    /**
     * SHA-1加密
     *
     * @param sourceString 待加密字符串
     * @return 编码后的字节数组
     */
    public static byte[] encrypt(String sourceString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(sourceString.getBytes(DEFAULT_CHARSET));
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
