package com.netease.lowcode.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class OpenFeignEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String,Object> config = new HashMap<>();
        config.put("feign.hystrix.enabled",true);
        config.put("hystrix.command.default.circuitBreaker.requestVolumeThreshold",2);
        config.put("hystrix.command.default.circuitBreaker.errorThresholdPercentage",0);

        environment.getPropertySources().addLast(new MapPropertySource("OPENFEIGNMAP",config));
    }
}
