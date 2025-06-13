package com.netease.lowcode.extensions.encrypt.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * 完整性计算
 */
public class SM3Util {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * sm3算法加密
     *
     * @param sourceStr
     * @return
     */
    public static byte[] encrypt(String sourceStr) {
        byte[] bytes = sourceStr.getBytes(DEFAULT_CHARSET);
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * sm3加密算法
     *
     * @param sourceStr
     * @param key
     * @return
     */
    public static byte[] encryptPlus(String sourceStr, String key) {
        byte[] sourceBytes = sourceStr.getBytes(DEFAULT_CHARSET);
        byte[] keyBytes = key.getBytes(DEFAULT_CHARSET);

        KeyParameter keyParameter = new KeyParameter(keyBytes);
        SM3Digest digest = new SM3Digest();
        HMac hMac = new HMac(digest);

        hMac.init(keyParameter);
        hMac.update(sourceBytes, 0, sourceBytes.length);
        byte[] hash = new byte[hMac.getMacSize()];

        hMac.doFinal(hash, 0);
        return hash;
    }
}
