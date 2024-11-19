package com.netease.lowcode.extensions.encrypt.util;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class SM4Util {

    protected static final String SM4_CIPHER = "SM4/ECB/PKCS5Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String key, String keyEncodeType, String sourceStr, String targetEncodeType) {
        byte[] keyBytes, inputBytes;
        if ("base64".equals(keyEncodeType)) {
            keyBytes = Base64.getDecoder().decode(key);
        } else if ("hex".equals(keyEncodeType)) {
            keyBytes = HexUtil.decode(key);
        } else {
            throw new RuntimeException("不支持的编码格式");
        }

        inputBytes = sourceStr.getBytes(StandardCharsets.UTF_8);

        byte[] encrypt = sm4core(true, keyBytes, inputBytes);

        if ("base64".equals(targetEncodeType)) {
            return new String(Base64.getEncoder().encode(encrypt), StandardCharsets.UTF_8);
        } else if ("hex".equals(targetEncodeType)) {
            return HexUtil.encode(encrypt).toLowerCase();
        } else {
            throw new RuntimeException("不支持的编码格式");
        }
    }

    public static String decrypt(String key, String keyEncodeType, String sourceStr, String sourceEncodeType) {
        byte[] keyBytes, inputBytes;
        if ("base64".equals(keyEncodeType)) {
            keyBytes = Base64.getDecoder().decode(key);
        } else if ("hex".equals(keyEncodeType)) {
            keyBytes = HexUtil.decode(key);
        } else {
            throw new RuntimeException("不支持的编码格式");
        }
        if ("base64".equals(sourceEncodeType)) {
            inputBytes = Base64.getDecoder().decode(sourceStr);
        } else if ("hex".equals(sourceEncodeType)) {
            inputBytes = HexUtil.decode(sourceStr);
        } else {
            throw new RuntimeException("不支持的编码格式");
        }

        byte[] decrypt = sm4core(false, keyBytes, inputBytes);

        return new String(decrypt, StandardCharsets.UTF_8);
    }

    /**
     * ECB填充模式
     *
     * @return
     * @throws InvalidCipherTextException
     */
    public static byte[] sm4core(boolean forEncryption, byte[] keyBytes, byte[] input) {

        try {

            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new SM4Engine());
            KeyParameter keyParameter = new KeyParameter(keyBytes);
            cipher.init(forEncryption, keyParameter);
            byte[] out = new byte[cipher.getOutputSize(input.length)];
            int len = cipher.processBytes(input, 0, input.length, out, 0);
            len += cipher.doFinal(out, len);
            byte[] result = new byte[len];
            System.arraycopy(out, 0, result, 0, len);

            return result;
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 生成密钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static String generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance("SM4", BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = new SecureRandom();
        kg.init(128, random);
        SecretKey secretKey = kg.generateKey();
        return new String(Base64.getEncoder().encode(secretKey.getEncoded()));
    }

}
