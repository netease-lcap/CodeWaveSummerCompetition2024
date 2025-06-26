package com.netease.lowcode.lib.api.config;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonEsSearchConfig {
//    /**
//     * es地址
//     */
//    @NaslConfiguration
//    @Value("${esClientHost}")
//    public String esClientHost;
//    /**
//     * es端口
//     */
//    @NaslConfiguration
//    @Value("${esClientPort}")
//    public String esClientPort;
    /**
     * es地址
     */
    @NaslConfiguration
    @Value("${esClientUris}")
    public String esClientUris;
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

    public String getEsClientUris() {
        return esClientUris;
    }

    public void setEsClientUris(String esClientUris) {
        this.esClientUris = esClientUris;
    }

    public String getEsClientUsername() {
        return esClientUsername;
    }

    public void setEsClientUsername(String esClientUsername) {
        this.esClientUsername = esClientUsername;
    }

    public String getEsClientPassword() {
        return esClientPassword;
    }

    public void setEsClientPassword(String esClientPassword) {
        this.esClientPassword = esClientPassword;
    }
}
