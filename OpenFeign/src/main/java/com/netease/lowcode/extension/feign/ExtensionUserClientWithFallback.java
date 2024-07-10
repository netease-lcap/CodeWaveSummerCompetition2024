package com.netease.lowcode.extension.feign;

import com.netease.lowcode.extension.service.ExtensionUserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "extensionUserClientWithFallback",
        url = "http://dev.nacos.cstest.lcap.codewave-test.163yun.com",
        fallback = ExtensionUserClientFallback.class)
public interface ExtensionUserClientWithFallback {

    @GetMapping("/rest/hello")
    String hello();
}
