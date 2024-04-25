package com.fdddf.shorturl;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="shorturl")
@Configuration
public class Config {
    @Value("${spring.redis.host}")
    @NaslConfiguration
    public String redisHost = "localhost";

    @Value("${spring.redis.port}")
    @NaslConfiguration
    public Integer redisPort = 6379;

    @Value("${spring.redis.password}")
    @NaslConfiguration
    public String redisPassword = "";


    @Value("${spring.datasource.url}")
    @NaslConfiguration
    public String mysqlUrl = "jdbc:mysql://localhost:3306/shorturl?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";

    @Value("${spring.datasource.username}")
    @NaslConfiguration
    public String mysqlUser = "root";

    @Value("${spring.datasource.password}")
    @NaslConfiguration
    public String mysqlPassword = "";


}
