package com.netease.lowcode.extensions.encrypt.util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * 非对称
 */
public class SM2Util {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String key, String keyEncodeType, String sourceStr, String targetEncodeType, SM2Engine.Mode mode) {
        byte[] bytes = sourceStr.getBytes(DEFAULT_CHARSET);
        byte[] encrypt = encrypt(key, keyEncodeType, bytes, targetEncodeType, mode);
        if ("base64".equals(targetEncodeType)) {
            return new String(Base64.getEncoder().encode(encrypt), DEFAULT_CHARSET);
        } else if ("hex".equals(targetEncodeType)) {
            return HexUtil.encode(encrypt).toLowerCase();
        } else {
            throw new RuntimeException("不支持的编码格式");
        }
    }

    public static String decrypt(String key, String keyEncodeType, String sourceStr, String sourceEncodeType, SM2Engine.Mode mode) {
        byte[] decode;
        if ("base64".equals(sourceEncodeType)) {
            byte[] bytes = sourceStr.getBytes(DEFAULT_CHARSET);
            decode = Base64.getDecoder().decode(bytes);
        } else if ("hex".equals(sourceEncodeType)) {
            decode = HexUtil.decode(sourceStr);
        } else {
            throw new RuntimeException("不支持的密文编码格式");
        }
        byte[] decrypt = decrypt(key, keyEncodeType, decode, mode);
        return new String(decrypt, DEFAULT_CHARSET);
    }

    /**
     * 公钥加密（公钥使用Base64编码）
     *
     * @param key
     * @param sources
     * @return
     */
    private static byte[] encrypt(String key, String keyEncodeType, byte[] sources,String targetEncodeType, SM2Engine.Mode mode) {

        try {
            BCECPublicKey localECPublicKey;
            if ("hex".equals(keyEncodeType)) {
                localECPublicKey = createPublicKeyByHex(key);
            } else if ("base64".equals(keyEncodeType)) {
                PublicKey publicKey = createPublicKeyByBase64(key);
                localECPublicKey = (BCECPublicKey) publicKey;
            } else {
                throw new RuntimeException("密钥编码格式不支持");
            }
            ECParameterSpec localECParameterSpec = localECPublicKey.getParameters();
            ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN());

            ECPublicKeyParameters ecPublicKeyParameters =  new ECPublicKeyParameters(localECPublicKey.getQ(), localECDomainParameters);

            //
            SM2Engine sm2Engine = new SM2Engine(new SM3Digest(), mode);
            sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));

            return sm2Engine.processBlock(sources, 0, sources.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 私钥解密（私钥为Base64编码）
     *
     * @param key
     * @param sources
     * @return
     */
    private static byte[] decrypt(String key,String keyEncodeType, byte[] sources, SM2Engine.Mode mode) {
        try {
            BCECPrivateKey bcecPrivateKey;
            if ("hex".equals(keyEncodeType)) {
                bcecPrivateKey = createPrivateKeyByHex(key);
            } else if ("base64".equals(keyEncodeType)) {
                PrivateKey privateKey = createPrivateKeyByBase64(key);
                bcecPrivateKey = (BCECPrivateKey) privateKey;
            } else {
                throw new RuntimeException("密钥编码格式不支持");
            }
            ECParameterSpec parameters = bcecPrivateKey.getParameters();
            ECDomainParameters ecDomainParameters = new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN());
            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(), ecDomainParameters);

            SM2Engine sm2Engine = new SM2Engine(new SM3Digest(), mode);
            sm2Engine.init(false, ecPrivateKeyParameters);

            return sm2Engine.processBlock(sources, 0, sources.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成sm2公私钥(经过Base64编码)
     * 0:公钥；1:私钥
     *
     * @return
     */
    public static String[] generateSmKey() {

        try {

            SecureRandom secureRandom = new SecureRandom();
            ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());

            keyPairGenerator.initialize(sm2Spec, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PrivateKey aPrivate = keyPair.getPrivate();
            PublicKey aPublic = keyPair.getPublic();

            // 0:公钥；1:私钥
            return new String[]{
                    new String(Base64.getEncoder().encode(aPublic.getEncoded())),
                    new String(Base64.getEncoder().encode(aPrivate.getEncoded()))
            };
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将base64编码的公钥转换为对象
     *
     * @param publicKey
     * @return
     */
    private static PublicKey createPublicKeyByBase64(String publicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static BCECPublicKey createPublicKeyByHex(String publicKey) {
        try {
            if (publicKey.length() > 128) {
                publicKey = publicKey.substring(publicKey.length() - 128);
            }

            String x = publicKey.substring(0, 64);
            String y = publicKey.substring(x.length());

            X9ECParameters sm2p256v1 = GMNamedCurves.getByName("sm2p256v1");
            ECParameterSpec ecParameterSpec = new ECParameterSpec(sm2p256v1.getCurve(), sm2p256v1.getG(), sm2p256v1.getN());
            ECPublicKeySpec spec = new ECPublicKeySpec(sm2p256v1.getCurve().createPoint(new BigInteger(x, 16), new BigInteger(y, 16)), ecParameterSpec);
            return new BCECPublicKey("EC", spec, BouncyCastleProvider.CONFIGURATION);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private static BCECPrivateKey createPrivateKeyByHex(String privateKey) {
        try {
            X9ECParameters sm2p256v1 = GMNamedCurves.getByName("sm2p256v1");
            ECParameterSpec ecParameterSpec = new ECParameterSpec(sm2p256v1.getCurve(), sm2p256v1.getG(), sm2p256v1.getN());
            ECPrivateKeySpec spec = new ECPrivateKeySpec(new BigInteger(privateKey, 16), ecParameterSpec);
            return new BCECPrivateKey("EC", spec, BouncyCastleProvider.CONFIGURATION);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 将base64编码的私钥转换为对象
     *
     * @param privateKey
     * @return
     */
    private static PrivateKey createPrivateKeyByBase64(String privateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static void test132() {
        String pub = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEv7TrZsItckV1+JZmR7ObAIruhLSLdRiBe60Wk/TwBgPVOY4GgG2foStQBunzGMcuvstCNNFPqfkQ1nImaksdPw==";
        String pri = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgVPcqd2ZWW8o2vVbkxGbAcBIqOIW7u9IbjhwIiqvRvO2gCgYIKoEcz1UBgi2hRANCAAS/tOtmwi1yRXX4lmZHs5sAiu6EtIt1GIF7rRaT9PAGA9U5jgaAbZ+hK1AG6fMYxy6+y0I00U+p+RDWciZqSx0/";

        String source = "{\"code\":200,\"data\":{\"accountId\":316180243108990976,\"valid\":true},\"faildType\":null,\"id\":null,\"msg\":\"成功\",\"success\":true}";
        String s1 = encrypt(pub,"base64", source,"base64",SM2Engine.Mode.C1C3C2);

        String s2 = decrypt(pri,"base64", s1,"base64",SM2Engine.Mode.C1C3C2);
    }
    //
    private static void test123() {
        String pub = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEv7TrZsItckV1+JZmR7ObAIruhLSLdRiBe60Wk/TwBgPVOY4GgG2foStQBunzGMcuvstCNNFPqfkQ1nImaksdPw==";
        String pri = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgVPcqd2ZWW8o2vVbkxGbAcBIqOIW7u9IbjhwIiqvRvO2gCgYIKoEcz1UBgi2hRANCAAS/tOtmwi1yRXX4lmZHs5sAiu6EtIt1GIF7rRaT9PAGA9U5jgaAbZ+hK1AG6fMYxy6+y0I00U+p+RDWciZqSx0/";

        String source = "test sm2 c1c2c3";
        String s1 = encrypt(pub,"base64", source,"base64",SM2Engine.Mode.C1C2C3);

        String s2 = decrypt(pri,"base64", s1,"base64",SM2Engine.Mode.C1C2C3);
    }

    public static void main(String[] args) {

        // 在线验证地址
        // https://the-x.cn/cryptography/Sm2.aspx

        test132();
        test123();
    }

}
