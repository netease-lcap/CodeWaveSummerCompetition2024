package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.custonapifilter.sign.SignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class Md5SignatureServiceImpl implements SignatureService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Override
    public Boolean signature(String data, String key, String sign) {
        try {
            data = key + data;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(data.getBytes("UTF-8"));
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                String encryptedDataString = sb.toString();
                // 比较生成的签名与提供的签名
                return encryptedDataString.equals(sign);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                log.error("md5 signature error", e);
            }
        } catch (Exception e) {
            log.error("aes signature error", e);
        }
        return false;
    }

    @Override
    public String type() {
        return "md5";
    }
}
