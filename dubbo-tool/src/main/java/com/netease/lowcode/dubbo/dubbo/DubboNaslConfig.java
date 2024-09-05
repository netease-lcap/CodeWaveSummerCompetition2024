package com.netease.lowcode.dubbo.dubbo;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Dubbo 工具类配置
 */
@Configuration
public class DubboNaslConfig {
    /**
     * dubbo应用配置，默认spring.application.name
     */
    @NaslConfiguration
    String dubboApplicationName;
    /**
     * dubbo注册中心配置，支持多个，用;分割
     */
    @NaslConfiguration
    String dubboRegistryAddress;
    /**
     * dubbo协议配置
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "dubbo"),
            @Environment(type = EnvironmentType.ONLINE, value = "dubbo")})
    String dubboProtocolName;
    /**
     * dubbo协议端口
     */
    @NaslConfiguration
    String dubboProtocolPort;
    /**
     * dubbo消费超时时间
     */
    String dubboConsumerTimeout;
    /**
     * dubbo消费重试次数
     */
    String dubboConsumerRetries;
    /**
     * dubbo消费负载均衡策略
     */
    @NaslConfiguration
    String dubboConsumerLoadbalance;
    /**
     * dubbo服务方超时时间
     */
    @NaslConfiguration
    String dubboProviderTimeout;
    /**
     * dubbo服务方重试次数
     */
    @NaslConfiguration
    String dubboProviderRetries;
    /**
     * dubbo服务方负载均衡策略
     */
    @NaslConfiguration
    String dubboProviderLoadbalance;
}
