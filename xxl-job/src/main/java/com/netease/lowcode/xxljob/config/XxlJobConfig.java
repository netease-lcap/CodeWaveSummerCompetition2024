package com.netease.lowcode.xxljob.config;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobConfig {

    /**
     * xxlJob地址
     */
    @Value("${adminAddress:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String adminAddress;

    /**
     * 应用名称
     */
    @Value("${appName:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String appName;

    /**
     * token
     */
    @Value("${accessToken:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String accessToken;

    /**
     * 执行器注册地址
     */
    @Value("${address:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String address;

    /**
     * ip
     */
    @Value("${ip:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String ip;

    /**
     * 端口
     */
    @Value("${port:}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = ""),
            @Environment(type = EnvironmentType.ONLINE, value = "")
    })
    public String port;


    /**
     * 日志地址
     */
    @Value("${logPath:logs/xxl-job/jobHandler}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "logs/xxl-job/jobHandler"),
            @Environment(type = EnvironmentType.ONLINE, value = "logs/xxl-job/jobHandler")
    })
    public String logPath;

    /**
     * 日志过期时间
     */
    @Value("${logRetentionDays:30}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "30"),
            @Environment(type = EnvironmentType.ONLINE, value = "30")
    })
    public String logRetentionDays;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setLogRetentionDays(String logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public String getAdminAddress() {
        return this.adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }



    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getLogPath() {
        return this.logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getLogRetentionDays() {
        return logRetentionDays;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
