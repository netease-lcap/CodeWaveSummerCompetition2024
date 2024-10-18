package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.custonapifilter.sign.SignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;


@Component
public class DesSignatureServiceImpl implements SignatureService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Override
    public Boolean signature(String data, String key, String sign) {
        try {
            // 将Base64编码的密钥解码
            byte[] decodedKey = Base64.getDecoder().decode(key);

            // 创建DESKeySpec
            KeySpec desKeySpec = new DESKeySpec(decodedKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // 创建DES的Cipher实例
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 加密数据
            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

            // 将加密后的数据以Base64编码
            String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData);
            log.info("生成的签名：" + encryptedDataString);
            // 比较生成的签名与提供的签名
            return encryptedDataString.equals(sign);
        } catch (Exception e) {
            log.warn("des signature error，验证签名失败", e);
            // 适当处理异常
            return false;
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
    public String decryptDes(String encryptedDataString, String key) {
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

    @Override
    public String type() {
        return "des";
    }
}
