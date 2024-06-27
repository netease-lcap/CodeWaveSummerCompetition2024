package com.captcha.config;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    /**
     * redis 地址
     */
    @NaslConfiguration
    @Value("${redisHost}")
    public String redisHost;
    /**
     * redis 端口
     */
    @NaslConfiguration
    @Value("${redisPort}")
    public Integer redisPort;

    /**
     * redis密码
     */
    @NaslConfiguration
    @Value("${redisPassword}")
    public String redisPassword;

}
