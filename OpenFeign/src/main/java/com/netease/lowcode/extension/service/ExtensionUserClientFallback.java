package com.netease.lowcode.extension.service;

import com.netease.lowcode.extension.feign.ExtensionUserClientWithFallback;
import org.springframework.stereotype.Component;

@Component
public class ExtensionUserClientFallback implements ExtensionUserClientWithFallback {

    @Override
    public String hello() {
        return "触发熔断";
    }
}
