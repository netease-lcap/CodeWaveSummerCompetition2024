package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.custonapifilter.config.Constants;
import com.netease.lowcode.custonapifilter.sign.CheckService;
import com.netease.lowcode.custonapifilter.storage.StorageNaslConfiguration;
import com.netease.lowcode.custonapifilter.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class NonceRedisCheckServiceImpl implements CheckService {

    Map<String, StorageService> storageServiceMap = new LinkedHashMap<>();
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private SignNaslConfiguration signNaslConfiguration;
    @Resource
    private List<StorageService> storageServices;
    @Resource
    private StorageNaslConfiguration storageNaslConfiguration;

    @PostConstruct
    public void init() {
        storageServices.forEach(storageService -> storageServiceMap.put(storageService.type(), storageService));
    }

    @Override
    public boolean check(HttpServletRequest request) {
        RequestHeader requestHeader = new RequestHeader(request.getHeader(Constants.LIB_SIGN_HEADER_NAME), request.getHeader(Constants.LIB_TIMESTAMP_HEADER_NAME), request.getHeader(Constants.LIB_NONCE_HEADER_NAME));
        if (requestHeader.getTimestamp() == null || requestHeader.getSign() == null || requestHeader.getNonce() == null) {
            log.info("无timestamp、nonce、sign信息");
            return false;
        }
        if ("1".equals(signNaslConfiguration.isCheckTimeStamp)) {
            //        判断当前时间和timestamp的关系。
            if (System.currentTimeMillis() - Long.parseLong(requestHeader.getTimestamp()) > Long.parseLong(signNaslConfiguration.signMaxTime)) {
                log.error("checkSign error，时间超出范围");
                return false;
            }
        }
//        校验timestamp、nonce和sign的关系。
        if (!checkSign(requestHeader)) {
            log.warn("checkSign error，签名校验失败");
            return false;
        }
        if (storageNaslConfiguration.storageStrategy == null) {
            log.error("storageStrategy error，配置信息异常");
            return false;
        }
//        查询redis看60s内是否存在
        StorageService storageService = storageServiceMap.get(storageNaslConfiguration.storageStrategy);
        if (storageService == null) {
            log.error("storageStrategy error，配置信息异常");
            return false;
        }
        Long timestamp;
        try {
            timestamp = Long.parseLong(signNaslConfiguration.signMaxTime);
        } catch (NumberFormatException e) {
            log.error("checkSign error，配置信息-时间格式异常", e);
            return false;
        }
        return storageService.checkAndAddIfAbsent(requestHeader.getNonce() + requestHeader.getTimestamp(), timestamp);
    }

    private boolean checkSign(RequestHeader requestHeader) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(signNaslConfiguration.secretKey);

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
            verifier.update((requestHeader.getTimestamp() + requestHeader.getNonce()).getBytes());
            return verifier.verify(Base64.getDecoder().decode(requestHeader.getSign()));
        } catch (SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            log.warn("checkSign error，验证签名失败", e);
            return false;
        }
    }

    public static class RequestHeader {
        private String sign;
        private String timestamp;
        private String nonce;

        public RequestHeader(String sign, String timestamp, String nonce) {
            this.sign = sign;
            this.timestamp = timestamp;
            this.nonce = nonce;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }
    }
}
