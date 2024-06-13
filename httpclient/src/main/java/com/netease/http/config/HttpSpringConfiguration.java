package com.netease.http.config;

import com.netease.http.util.RestTemplateUtil;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpSpringConfiguration {
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 默认的restTemplate
     *
     * @return
     */
    @Bean("restTemplatePrimary")
    public RestTemplate restTemplatePrimary() {
        HttpComponentsClientHttpRequestFactory requestFactory = RestTemplateUtil.createRequestFactory();
        requestFactory.setHttpClient(HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build());
        return new RestTemplate(requestFactory);
    }

    /**
     * 信任所有证书的restTemplate
     *
     * @return
     */
    @Bean(name = "restTemplateIgnoreCrt")
    public RestTemplate restTemplateIgnoreCrt() {
        HttpComponentsClientHttpRequestFactory requestFactory = RestTemplateUtil.createCustomRequestFactoryIgnoreCrt();
        requestFactory.setHttpClient(HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build());
        return new RestTemplate(requestFactory);
    }
}