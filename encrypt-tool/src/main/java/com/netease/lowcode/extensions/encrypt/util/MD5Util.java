package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加密
 */
public class MD5Util {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private MD5Util() {

    }

    /**
     * MD5加密
     *
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithMD5(String sourceString) {
        return DatatypeConverter.printHexBinary(encrypt(sourceString)).toLowerCase();
    }

    /**
     * MD5加密
     *
     * @param sourceString 待加密字符串
     * @return 编码后的字节数组
     */
    public static byte[] encrypt(String sourceString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(sourceString.getBytes(DEFAULT_CHARSET));
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
