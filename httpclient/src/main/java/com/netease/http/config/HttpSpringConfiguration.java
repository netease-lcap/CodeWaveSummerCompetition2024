package com.netease.http.config;

import com.netease.http.util.RestTemplateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpSpringConfiguration {

    /**
     * 默认的restTemplate
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 信任所有证书的restTemplate
     *
     * @return
     */
    @Bean(name = "restTemplateIgnoreCrt")
    public RestTemplate restTemplateIgnoreCrt() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(RestTemplateUtil.createCustomRequestFactoryIgnoreCrt());
        return restTemplate;
    }
}