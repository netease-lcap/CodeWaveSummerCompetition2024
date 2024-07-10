package com.netease.lowcode.extension;

import feign.hystrix.HystrixFeign;
import org.springframework.context.annotation.Bean;

// 不用加 @Configuration
public class OpenFeignCustomConfig {

    /**
     * 开启熔断器
     * feign.hystrix.enabled = true
     *
     * @return
     */
    @Bean
    public HystrixFeign.Builder feignHystrixBuilder() {
        return HystrixFeign.builder();
    }

}
