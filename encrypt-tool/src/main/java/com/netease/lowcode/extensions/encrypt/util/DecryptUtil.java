package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.bouncycastle.crypto.engines.SM2Engine;

/**
 * 解密工具类
 */
public class DecryptUtil {
    private DecryptUtil() {

    }

    /**
     * 先base64解密，再des解密
     *
     * @param key             密钥
     * @param encryptedString 待解密字符串
     * @return 解密后的字符串
     */
    @NaslLogic
    public static String decryptWithBase64AndDes(String key, String encryptedString) {
        byte[] bytes = Base64Util.getDecryptBytes(Base64Util.decrypt(encryptedString));

        return DesUtil.decrypt(key, bytes);
    }

    /**
     * 先base64解密，再aes解密
     *
     * @param key             密钥
     * @param encryptedString 待解密字符串
     * @return 解密后的字符串
     */
    @NaslLogic
    public static String decryptWithAESAndBase64(String key, String encryptedString) {
        byte[] bytes = Base64Util.getDecryptBytes(Base64Util.decrypt(encryptedString));

        return AESUtil.decrypt(key, bytes);
    }

    /**
     * 先使用hex解码，然后使用mysql格式的aes解密
     * SELECT CONVERT(AES_DECRYPT(UNHEX('ADE307F5EF0053889107C28441170072'), 'mykey') USING utf8);
     *
     * @param key
     * @param encryptedString
     * @return
     */
    @NaslLogic
    public static String decryptWithMySqlAESAndHex(String key, String encryptedString) {
        byte[] decode = HexUtil.decode(encryptedString);
        return AESUtil.mysqlDecrypt(key, decode);
    }

    /**
     * base64解密
     *
     * @param encryptedString 待解密字符串
     * @return 解密后的字符串
     */
    @NaslLogic
    public static String decryptWithBase64(String encryptedString) {
        return Base64Util.decrypt(encryptedString);
    }

    /**
     * 国密sm2 c1c3c2 解密，密文必须是base64编码
     *
     * @param privateKey
     * @param base64Str
     * @return
     */
    @NaslLogic
    public static String decryptWithSM2C132(String privateKey, String keyEncodeType, String base64Str, String sourceEncodeType) {
        return SM2Util.decrypt(privateKey, keyEncodeType, base64Str, sourceEncodeType, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 国密sm2 c1c2c3 解密，密文必须是base64编码
     *
     * @param privateKey
     * @param base64Str
     * @return
     */
    @NaslLogic
    public static String decryptWithSM2C123(String privateKey, String keyEncodeType, String base64Str, String sourceEncodeType) {
        return SM2Util.decrypt(privateKey, keyEncodeType, base64Str, sourceEncodeType, SM2Engine.Mode.C1C2C3);
    }

    /**
     * 国密sm4 解密
     *
     * @param key
     * @param keyEncodeType
     * @param base64Str
     * @param sourceEncodeType
     * @return
     */
    @NaslLogic
    public static String decryptWithSM4(String key, String keyEncodeType, String base64Str, String sourceEncodeType) {
        return SM4Util.decrypt(key, keyEncodeType, base64Str, sourceEncodeType);
    }
}

