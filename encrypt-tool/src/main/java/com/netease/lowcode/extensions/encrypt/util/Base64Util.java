package com.netease.lowcode.extensions.encrypt.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * base64加密解密，
 * 大部分给出的第三方接口base64加解密使用的是{@link sun.misc.BASE64Encoder}和{@link sun.misc.BASE64Decoder},
 * 这两个类使用的字符集为ISO-8859-1，所以默认实现了此字符集的base64编解码
 */
public class Base64Util {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    private Base64Util() {

    }

    /**
     * Base64进行加密
     *
     * @param sourceString 待加密字符串
     * @return 编码为ISO_8859_1的加密字符串
     */
    public static String encrypt(String sourceString) {
        if (Objects.isNull(sourceString) || sourceString.length() == 0) {
            return "";
        }
        return new String(encrypt(sourceString.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
    }

    /**
     * Base64进行解密
     *
     * @param encryptedString 待解密字符串
     * @return 编码为ISO_8859_1的解密字符串
     */
    public static String decrypt(String encryptedString) {
        if (Objects.isNull(encryptedString) || encryptedString.length() == 0) {
            return "";
        }

        return new String(decrypt(encryptedString.replaceAll("\\s", "").getBytes(DEFAULT_CHARSET)), StandardCharsets.UTF_8);
    }

    /**
     * 将待加密字节数组转换为待加密字符串
     *
     * @param sourceBytes 待加密数组
     * @return 待加密字符串
     */
    public static String getSourceString(byte[] sourceBytes) {
        return new String(sourceBytes, DEFAULT_CHARSET);
    }

    /**
     * 将解密后的字符串转换为字节数组
     *
     * @param decryptString 解密后的字符串
     * @return 解密数组
     */
    public static byte[] getDecryptBytes(String decryptString) {
        return decryptString.getBytes(DEFAULT_CHARSET);
    }

    /**
     * 通过base64将给定字节数组进行编码
     *
     * @param src 原始字节数组
     * @return 编码后的字节数组
     */
    private static byte[] encrypt(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    /**
     * 通过base64将给定字节数组进行解码
     *
     * @param src 编码后的字节数组
     * @return 原始字节数组
     */
    private static byte[] decrypt(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }
}

