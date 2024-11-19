package com.netease.lowcode.extensions.encrypt.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * HMAC-Sha256加解密工具类
 */
public class HMACSha256Util {
    private static final Charset charset = StandardCharsets.UTF_8;

    private HMACSha256Util() {

    }

    /**
     * HMAC-Sha256 加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    public static byte[] encrypt(String key, String sourceString) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(charset), "HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(sourceString.getBytes(charset));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
    }
}