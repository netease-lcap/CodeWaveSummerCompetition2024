package com.netease.lowcode.extensions.encrypt.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESUtil {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final String AES_CIPHER = "AES/ECB/PKCS5Padding";

    private AESUtil() {

    }

    /**
     * 通过aes进行加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(String key, String sourceString) {
        return CipherUtil.cipher(AES_CIPHER, key, Cipher.ENCRYPT_MODE, sourceString.getBytes(DEFAULT_CHARSET));
    }

    /**
     * 通过aes进行解密
     *
     * @param key            密钥
     * @param encryptedBytes 待解密字节数组
     * @return 解密后的字符串
     */
    public static String decrypt(String key, byte[] encryptedBytes) {
        return new String(CipherUtil.cipher(AES_CIPHER, key, Cipher.DECRYPT_MODE, encryptedBytes), DEFAULT_CHARSET);
    }

    /**
     * mysql格式的 aes加密
     * select hex(AES_ENCRYPT('明文','密钥'));
     *
     * @param key
     * @param sourceString
     * @return
     */
    public static byte[] mysqlEncrypt(String key, String sourceString) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, generateMysqlAESKey(key));
            return cipher.doFinal(sourceString.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * mysql格式的 aes解密
     * SELECT CONVERT(AES_DECRYPT(UNHEX('ADE307F5EF0053889107C28441170072'), 'mykey') USING utf8);
     *
     * @param key
     * @param encryptedBytes
     * @return
     */
    public static String mysqlDecrypt(String key, byte[] encryptedBytes) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, generateMysqlAESKey(key));
            byte[] bytes = cipher.doFinal(encryptedBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按照mysql加密方式填充密钥
     *
     * @param originKey
     * @return
     */
    private static SecretKeySpec generateMysqlAESKey(String originKey) {
        final byte[] key = new byte[16];
        int i = 0;
        for (byte b : originKey.getBytes(StandardCharsets.UTF_8)) {
            key[i++ % 16] ^= b;
        }
        return new SecretKeySpec(key, "AES");
    }
}

