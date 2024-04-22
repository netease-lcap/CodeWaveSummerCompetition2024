package com.fdddf.emailfetcher;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {
    @Value("${protocol}")
    @NaslConfiguration
    public String protocol;

    @Value("${sslEnable}")
    @NaslConfiguration
    public Boolean sslEnable;

    @Value("${host}")
    @NaslConfiguration
    public String host;

    @Value("${port}")
    @NaslConfiguration
    public String port;

    @Value("${username}")
    @NaslConfiguration
    public String username;

    @Value("${password}")
    @NaslConfiguration
    public String password;

}
