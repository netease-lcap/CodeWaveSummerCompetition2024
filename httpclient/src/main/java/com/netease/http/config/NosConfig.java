package com.netease.http.config;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NosConfig {

    @NaslConfiguration
    @Value("${nosBucket}")
    public String nosBucket;
    @NaslConfiguration
    @Value("${nosAccessKey}")
    public String nosAccessKey;
    @NaslConfiguration
    @Value("${nosSecretKey}")
    public String nosSecretKey;
    @NaslConfiguration
    @Value("${nosAddress}")
    public String nosAddress;
    @NaslConfiguration
    @Value("${sinkType}")
    public String sinkType;
}
