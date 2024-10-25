package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.custonapifilter.sign.SignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


@Component
public class AesSignatureServiceImpl implements SignatureService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Override
    public Boolean signature(String data, String key, String sign) {
        try {
            // 将Base64编码的密钥解码
            byte[] decodedKey = Base64.getDecoder().decode(key);
            Key secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // 创建AES的Cipher实例
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 加密数据
            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

            // 将加密后的数据以Base64编码
            String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData);

            // 比较生成的签名与提供的签名
            return encryptedDataString.equals(sign);
        } catch (Exception e) {
            log.error("aes signature error", e);
            // 适当处理异常
            return false;
        }
    }

    /**
     * AES解密
     *
     * @param encryptedDataString
     * @param key
     * @return
     */
    @NaslLogic
    public String decryptAES(String encryptedDataString, String key) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            Key secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataString);
            byte[] decryptedData = cipher.doFinal(encryptedData);

            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String type() {
        return "aes";
    }
}
