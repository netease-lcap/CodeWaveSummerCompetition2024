package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Des加密解密
 */
public class DesUtil {
    protected static final String DES_CIPHER = "DESede/ECB/PKCS5Padding";
    protected static final String DES_CIPHER2 = "DES/CBC/PKCS5Padding";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private DesUtil() {

    }

    /**
     * 通过des进行加密
     *
     * @param key          密钥（24字节）
     * @param sourceString 待加密字符串
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(String key, String sourceString) {
        return CipherUtil.cipher(DES_CIPHER, key, Cipher.ENCRYPT_MODE, sourceString.getBytes(DEFAULT_CHARSET));
    }

    /**
     * 通过des进行解密
     *
     * @param key            密钥（24字节）
     * @param encryptedBytes 待解密字节数组
     * @return 解密后的字符串
     */
    public static String decrypt(String key, byte[] encryptedBytes) {
        return new String(CipherUtil.cipher(DES_CIPHER, key, Cipher.DECRYPT_MODE, encryptedBytes), DEFAULT_CHARSET);
    }

    /**
     * 通过des进行加密
     *
     * @param key8         密钥（8字节）
     * @param sourceString 待加密字符串
     * @return 加密后的字节数组
     */
    public static byte[] encrypt2(String key8, String sourceString) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key8.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key8.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(sourceString.getBytes(StandardCharsets.UTF_8));
            return bytes;
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES解密
     *
     * @param encryptedDataString
     * @param key
     * @return
     */
    @NaslLogic
    public static String decryptDes(String encryptedDataString, String key) {
        try {
            // 将Base64编码的密钥解码
            byte[] decodedKey = Base64.getDecoder().decode(key);
            // 创建DESKeySpec
            KeySpec desKeySpec = new DESKeySpec(decodedKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            // 创建DES的Cipher实例
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            // 解密数据
            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataString);
            byte[] decryptedData = cipher.doFinal(encryptedData);

            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            // 适当处理异常
            return null;
        }
    }
}
