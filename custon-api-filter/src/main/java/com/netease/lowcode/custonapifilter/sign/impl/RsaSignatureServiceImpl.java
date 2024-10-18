package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.custonapifilter.sign.SignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaSignatureServiceImpl implements SignatureService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Override
    public Boolean signature(String data, String key, String sign) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(key);

        // 转换公钥字节数组为PublicKey对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory; // 或者其他算法
        Signature verifier;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            verifier = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("checkSign error，加密算法或环境异常", e);
            return false;
        }
        try {
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            verifier.initVerify(publicKey);
            verifier.update(data.getBytes());
            return verifier.verify(Base64.getDecoder().decode(sign));
        } catch (SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            log.warn("checkSign error，验证签名失败", e);
            return false;
        }
    }

    @Override
    public String type() {
        return "rsa";
    }
}
