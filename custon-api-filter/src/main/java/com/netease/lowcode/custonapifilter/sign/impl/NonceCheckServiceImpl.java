package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.custonapifilter.sign.CheckService;
import com.netease.lowcode.custonapifilter.sign.SignatureService;
import com.netease.lowcode.custonapifilter.sign.dto.RequestHeader;
import com.netease.lowcode.custonapifilter.storage.StorageNaslConfiguration;
import com.netease.lowcode.custonapifilter.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class NonceCheckServiceImpl implements CheckService {

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    Map<String, StorageService> storageServiceMap = new LinkedHashMap<>();
    Map<String, SignatureService> signatureServiceMap = new LinkedHashMap<>();
    @Resource
    private SignNaslConfiguration signNaslConfiguration;
    @Resource
    private List<StorageService> storageServices;
    @Resource
    private List<SignatureService> signatureServices;
    @Resource
    private StorageNaslConfiguration storageNaslConfiguration;

    @PostConstruct
    public void init() {
        storageServices.forEach(storageService -> storageServiceMap.put(storageService.type(), storageService));
        signatureServices.forEach(signatureService -> signatureServiceMap.put(signatureService.type(), signatureService));
    }

    @Override
    public boolean check(RequestHeader requestHeader) {
        if (requestHeader.getTimestamp() == null || requestHeader.getSign() == null || requestHeader.getNonce() == null) {
            log.warn("无timestamp、nonce、sign信息");
            return false;
        }
        if ("1".equals(signNaslConfiguration.getIsCheckTimeStamp())) {
            //        判断当前时间和timestamp的关系。
            if (System.currentTimeMillis() - Long.parseLong(requestHeader.getTimestamp()) > Long.parseLong(signNaslConfiguration.getSignMaxTime())) {
                log.error("checkSign error，时间超出范围");
                return true;
            }
        }
//        校验timestamp、nonce和sign的关系。新增body
        if (!checkSign(requestHeader)) {
            log.warn("checkSign error，签名校验失败");
            return true;
        }
        if (storageNaslConfiguration.getStorageStrategy() == null) {
            log.error("storageStrategy error，配置信息异常");
            return true;
        }
        StorageService storageService = storageServiceMap.get(storageNaslConfiguration.getStorageStrategy());
        if (storageService == null) {
            log.error("storageStrategy error，配置信息异常");
            return true;
        }
        Long timestamp;
        try {
            timestamp = Long.parseLong(signNaslConfiguration.getSignMaxTime());
        } catch (NumberFormatException e) {
            log.error("checkSign error，配置信息-时间格式异常", e);
            return true;
        }
//        查看60s内是否存在
        storageService.checkAndAddIfAbsent(requestHeader.getNonce() + requestHeader.getTimestamp(), timestamp);
        return true;
    }


    private boolean checkSign(RequestHeader requestHeader) {
        if (storageNaslConfiguration.getSignatureStrategy() == null) {
            log.error("signatureStrategy error，配置信息异常");
            return false;
        }
        SignatureService signatureService = signatureServiceMap.get(storageNaslConfiguration.getSignatureStrategy());
        if (signatureService == null) {
            log.error("signatureStrategy error，配置信息异常");
            return false;
        }
        String key = signNaslConfiguration.getSecretKey();
        String data = requestHeader.getTimestamp() + requestHeader.getNonce() + requestHeader.getBody();
        String sign = requestHeader.getSign();
        return signatureService.signature(data, key, sign);
    }
}
