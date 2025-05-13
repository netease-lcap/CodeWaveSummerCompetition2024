package com.netease.lowcode.lib.api.config;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonEsSearchConfig {
    /**
     * es地址
     */
    @NaslConfiguration
    @Value("${esClientHost}")
    public String esClientHost;
    /**
     * es端口
     */
    @NaslConfiguration
    @Value("${esClientPort}")
    public String esClientPort;
    /**
     * es用户名
     */
    @NaslConfiguration
    @Value("${esClientUsername}")
    public String esClientUsername;
    /**
     * es密码
     */
    @NaslConfiguration
    @Value("${esClientPassword}")
    public String esClientPassword;

}
