package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.bouncycastle.crypto.engines.SM2Engine;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密工具类
 */
public class EncryptUtil {

    private EncryptUtil() {

    }

    /**
     * 进行一次des加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串(Hex)
     */
    @NaslLogic
    public static String encryptWithDes(String key, String sourceString) {
        byte[] buffer = DesUtil.encrypt(key, sourceString);

        return HexUtil.encode(buffer);
    }

    /**
     * 进行一次DES/CBC/PKCS5Padding des加密
     *
     * @param key8 密钥（这里要求长度8个字符）
     * @param sourceString 待加密字符串
     * @return 加密后的字符串hex
     */
    @NaslLogic
    public static String encryptWithDesAndHex(String key8, String sourceString) {
        byte[] bytes = DesUtil.encrypt2(key8, sourceString);
        return HexUtil.encode2(bytes);
    }

    /**
     * 先通过des加密，再进行base64加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithDesAndBase64(String key, String sourceString) {
        byte[] buffer = DesUtil.encrypt(key, sourceString);

        return Base64Util.encrypt(Base64Util.getSourceString(buffer));
    }

    /**
     * 先通过MD5加密，再进行base64加密
     *
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithMD5AndBase64(String sourceString) {
        byte[] buffer = MD5Util.encrypt(sourceString);

        return Base64Util.encrypt(Base64Util.getSourceString(buffer));
    }

    /**
     * 先通过SHA1加密，再转换为16进制字符串，并将所有字符串转为小写
     *
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithSHA1AndHexLowerCase(String sourceString) {
        byte[] buffer = SHA1Util.encrypt(sourceString);

        return HexUtil.encode(buffer).toLowerCase();
    }

    /**
     * SM3加密后转16进制小写
     *
     * @param sourceStr
     * @return
     */
    @NaslLogic
    public static String encryptWithSM3AndHexLowerCase(String sourceStr) {
        byte[] buffer = SM3Util.encrypt(sourceStr);

        return HexUtil.encode(buffer).toLowerCase();
    }

    /**
     * 先通过HMAC-Sha256进行加密，再进行base64加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithSHA256AndBase64(String key, String sourceString) {
        byte[] buffer = HMACSha256Util.encrypt(key, sourceString);

        return Base64Util.encrypt(Base64Util.getSourceString(buffer));
    }

    /**
     * 先通过MD5进行加密再进行HMAC-sha1加密最后用base64加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithMD5AndSHA1Base64(String key, String sourceString) {
        byte[] buffer = HMACSha1Util.encrypt(key, MD5Util.encryptWithMD5(sourceString));

        return Base64Util.encrypt(Base64Util.getSourceString(buffer));
    }

    /**
     * 先通过HMAC-sha256进行加密再转换为16进制字符串，并将所有字符串转为小写
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithSHA256AndHexLowerCase(String key, String sourceString) {
        byte[] buffer = HMACSha256Util.encrypt(key, sourceString);

        return HexUtil.encode(buffer).toLowerCase();
    }

    /**
     * sha256加密后取前八个字节并转换为16进制小写字符串
     *
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithSHA256AndGetTopEightToHexLowerCase(String sourceString) {
        byte[] buffer = SHA256Util.encrypt(sourceString);

        byte[] key = new byte[8];
        System.arraycopy(buffer, 0, key, 0, 8);

        return HexUtil.encode(key).toLowerCase();

    }

    /**
     * sha256加密后并转换为16进制小写字符串
     *
     * @param sourceString
     * @return
     */
    @NaslLogic
    public static String encryptWithSHA256ToHexLowerCase(String sourceString) {
        byte[] buffer = SHA256Util.encrypt(sourceString);
        return HexUtil.encode(buffer).toLowerCase();
    }

    /**
     * 先通过aes加密，再进行base64加密
     *
     * @param key          密钥
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithAESAndBase64(String key, String sourceString) {
        byte[] buffer = AESUtil.encrypt(key, sourceString);

        return Base64Util.encrypt(Base64Util.getSourceString(buffer));
    }

    /**
     * mysql格式的aes加密，然后hex编码
     * select hex(AES_ENCRYPT('明文','密钥'));
     *
     * @param key
     * @param sourceString
     * @return
     */
    @NaslLogic
    public static String encryptWithMySqlAESAndHex(String key, String sourceString) {
        byte[] bytes = AESUtil.mysqlEncrypt(key, sourceString);
        return HexUtil.encode(bytes);
    }

    /**
     * base64加密
     *
     * @param sourceString 待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String encryptWithBase64(String sourceString) {
        return Base64Util.encrypt(sourceString);
    }

    /**
     * 国密sm2 c1c3c2 加密，base64编码
     *
     * @param publicKey
     * @param sourceStr
     * @return
     */
    @NaslLogic
    public static String encryptWithSM2C132(String publicKey, String keyEncodeType, String sourceStr, String targetEncodeType) {
        return SM2Util.encrypt(publicKey, keyEncodeType, sourceStr, targetEncodeType, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 国密sm2 c1c2c3 加密，base64编码
     *
     * @param publicKey
     * @param sourceStr
     * @return
     */
    @NaslLogic
    public static String encryptWithSM2C123(String publicKey, String keyEncodeType, String sourceStr, String targetEncodeType) {
        return SM2Util.encrypt(publicKey, keyEncodeType, sourceStr, targetEncodeType, SM2Engine.Mode.C1C2C3);
    }

    /**
     * 国密sm4 加密
     *
     * @param key
     * @param keyEncodeType
     * @param sourceStr
     * @param targetEncodeType
     * @return
     */
    @NaslLogic
    public static String encryptWithSM4(String key, String keyEncodeType, String sourceStr, String targetEncodeType) {
        return SM4Util.encrypt(key, keyEncodeType, sourceStr, targetEncodeType);
    }

    /**
     * 国密sm3 完整性算法，base64编码
     *
     * @param sourceStr
     * @return
     */
    @NaslLogic
    public static String encryptWithSM3Base64(String sourceStr) {
        byte[] encrypt = SM3Util.encrypt(sourceStr);
        return new String(Base64.getEncoder().encode(encrypt), StandardCharsets.UTF_8);
    }
}
