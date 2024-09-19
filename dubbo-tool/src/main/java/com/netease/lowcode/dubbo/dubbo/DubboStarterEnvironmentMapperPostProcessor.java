package com.netease.lowcode.dubbo.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


@Order
public class DubboStarterEnvironmentMapperPostProcessor implements EnvironmentPostProcessor {
    private static final Map<String, String> DUBBO_CONFIG_MAPPER = new HashMap<>();

    static {
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboApplicationName", "dubbo.application.name");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboRegistryAddress", "dubbo.registry.address");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboProtocolName", "dubbo.protocol.name");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboProtocolPort", "dubbo.protocol.port");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboConsumerTimeout", "dubbo.consumer.timeout");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboConsumerRetries", "dubbo.consumer.retries");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboConsumerLoadbalance", "dubbo.consumer.loadbalance");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboProviderTimeout", "dubbo.provider.timeout");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboProviderRetries", "dubbo.provider.retries");
        DUBBO_CONFIG_MAPPER.put("extensions.spring_boot_starter_dubbo_tool.custom.dubboProviderLoadbalance", "dubbo.provider.loadbalance");
    }

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> mapperProperties = new HashMap<>();
        DUBBO_CONFIG_MAPPER.forEach((key, value) -> {
            if (environment.containsProperty(value)) {
                return;
            }
            if (environment.containsProperty(key)) {
                String property = environment.getProperty(key);
                if (!StringUtils.isEmpty(property)) {
                    mapperProperties.put(value, property);
                } else if (key.contains("dubboApplicationName")) {
                    mapperProperties.put(value, environment.getProperty("spring.application.name"));
                }
            }
        });

        if (mapperProperties.isEmpty()) {
            return;
        }
        environment.getPropertySources().addLast(new MapPropertySource("DUBBO_CONFIG_MAPPER", mapperProperties));
    }
}
