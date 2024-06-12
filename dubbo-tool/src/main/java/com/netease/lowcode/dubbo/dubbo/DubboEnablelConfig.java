package com.netease.lowcode.dubbo.dubbo;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 开启Dubbo
 */
@Configuration
@EnableDubbo
public class DubboEnablelConfig {
    /**
     * 构造方法
     *
     * @param registryConfigs
     */
    public DubboEnablelConfig(List<RegistryConfig> registryConfigs) {
        System.out.println("EnableDubboConfig start");
    }
}
