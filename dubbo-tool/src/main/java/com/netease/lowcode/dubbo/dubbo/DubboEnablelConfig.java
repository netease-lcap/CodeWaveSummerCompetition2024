package com.netease.lowcode.dubbo.dubbo;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 开启Dubbo
 */
@Configuration
@EnableDubbo
public class DubboEnablelConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 构造方法
     *
     * @param registryConfigs
     */
    public DubboEnablelConfig(List<RegistryConfig> registryConfigs) {
        LOGGER.info("EnableDubboConfig start");
    }
}
