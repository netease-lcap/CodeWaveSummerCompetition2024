package com.netease.http.util;

import com.netease.http.config.CrtConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Component
public class RestTemplateUtil {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);
    public static CrtConfig config;

    /**
     * 创建自默认的ClientHttpRequestFactory
     *
     * @return 返回默认的ClientHttpRequestFactory
     */
    public static HttpComponentsClientHttpRequestFactory createRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }

    /**
     * 创建自定义的忽略证书验证的ClientHttpRequestFactory
     *
     * @return 返回自定义的ClientHttpRequestFactory
     */
    public static HttpComponentsClientHttpRequestFactory createCustomRequestFactoryIgnoreCrt() {
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

}
