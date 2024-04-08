package com.netease.http.util;

import com.alibaba.fastjson.JSONObject;
import com.netease.http.config.CrtConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@Component
public class RestTemplateUtil {
    public static CrtConfig config;
    private static final Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);

    /**
     * 创建自定义的忽略证书验证的ClientHttpRequestFactory
     *
     * @return 返回自定义的ClientHttpRequestFactory
     */
    public static ClientHttpRequestFactory createCustomRequestFactoryIgnoreCrt() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
        } catch (Exception e) {
            log.error("", e);
        }

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    /**
     * 创建带有证书的ClientHttpRequestFactory实例
     *
     * @return ClientHttpRequestFactory实例
     */
    private static ClientHttpRequestFactory createCustomRequestFactoryWithCrt(InputStream keyStoreInputStream) {
        SSLContext sslContext = null;
        //KeyStore.getDefaultType()
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyStoreInputStream, "keystorePassword".toCharArray());
            sslContext = SSLContextBuilder.create()
                    .loadKeyMaterial(keyStore, "keystorePassword".toCharArray())
                    .build();
        } catch (Exception e) {
            log.error("", e);
        }
        return new HttpComponentsClientHttpRequestFactory(HttpClients.custom().setSSLContext(sslContext).build());
    }

    /**
     * 创建一个RestTemplate对象
     *
     * @param isIgnoreCrt         是否忽略Crt
     * @return 创建的RestTemplate对象
     */
    public static RestTemplate createRestTemplate(boolean isIgnoreCrt) {
         if (!isIgnoreCrt) {
            //普通请求
            return new RestTemplate();
        } else {
            RestTemplate restTemplate=  new RestTemplate();
            restTemplate.setRequestFactory(createCustomRequestFactoryIgnoreCrt());
            //忽略证书请求
            return new RestTemplate(createCustomRequestFactoryIgnoreCrt());
        }
    }


    @Autowired
    public void setConfig(CrtConfig config) {
        RestTemplateUtil.config = config;
        log.info(JSONObject.toJSONString(RestTemplateUtil.config));
    }
}
