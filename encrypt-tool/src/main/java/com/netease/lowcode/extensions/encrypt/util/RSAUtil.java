package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.core.codec.Base64;

/**
 * @author zhuzaishao
 * @date 2022/8/16 11:45
 */
public class RSAUtil {
    @NaslLogic
    public static String signByPublicKeyWithRSA(String data, String base64PublicKey) {
        String publicKey = Base64Util.decrypt(base64PublicKey);
        RSA rsa = new RSA(null, publicKey);
        return Base64.encodeUrlSafe(rsa.encrypt(data.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey));
    }

    /**
     * RSA私钥签名
     *
     * @param base64PrivateKey 私钥
     * @param sourceString     待加密字符串
     * @return 加密后的字符串
     */
    @NaslLogic
    public static String signByPrivateKeyWithRSA(String base64PrivateKey, String sourceString) {
        try {
            byte[] keyBytes = getPrivateKey(base64PrivateKey).getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(key);
            signature.update(sourceString.getBytes(StandardCharsets.UTF_8));
            return new BASE64Encoder().encodeBuffer(signature.sign()).replaceAll("[\\s*\t\n\r]", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PrivateKey getPrivateKey(String base64PrivateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = new BASE64Decoder().decodeBuffer(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }
}
