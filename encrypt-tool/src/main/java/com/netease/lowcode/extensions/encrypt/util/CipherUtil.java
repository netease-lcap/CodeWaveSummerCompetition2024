package com.netease.lowcode.extensions.encrypt.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class CipherUtil {
    protected static final Map<String, Function<String, SecretKey>> KEY_MAPPER = new LinkedHashMap<>();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static {
        CipherUtil.KEY_MAPPER.put(AESUtil.AES_CIPHER, key -> new SecretKeySpec(key.getBytes(DEFAULT_CHARSET), "AES"));

        CipherUtil.KEY_MAPPER.put(DesUtil.DES_CIPHER, key -> {
            DESedeKeySpec dks;
            try {
                dks = new DESedeKeySpec(key.getBytes(DEFAULT_CHARSET));

                SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
                return factory.generateSecret(dks);
            } catch (InvalidKeyException e) {
                throw new IllegalArgumentException("key不能为空并且需要在24位以上");
            } catch (Exception e) {
                throw new IllegalArgumentException("无法获取secret key。");
            }
        });
    }

    private CipherUtil() {

    }

    /**
     * 加解密计算
     *
     * @param key        密钥
     * @param cipherType 加密方式
     * @param cipherMode 计算模式，加密或解密
     * @param bytes      待处理字节数组
     * @return 计算后的字节数组
     */
    public static byte[] cipher(String cipherType, String key, int cipherMode, byte[] bytes) {
        try {
            SecretKey secretKey = KEY_MAPPER.get(cipherType).apply(key);

            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(cipherMode, secretKey);

            return cipher.doFinal(bytes);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密钥
     *
     * @param cipherType 加密方式
     * @param key        密钥
     * @return 密钥
     * @throws Exception 无法生成密钥
     */
    public static SecretKey getSecretKey(String cipherType, String key) throws Exception {
        DESedeKeySpec dks;
        try {
            dks = new DESedeKeySpec(key.getBytes(DEFAULT_CHARSET));
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("key不能为空并且需要在24位以上");
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance(cipherType);
        return factory.generateSecret(dks);
    }
}

