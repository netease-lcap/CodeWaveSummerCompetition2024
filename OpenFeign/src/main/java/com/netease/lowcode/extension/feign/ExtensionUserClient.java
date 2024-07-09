package com.netease.lowcode.extension.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="extensionUserClient",url = "http://dev.nacos.cstest.lcap.codewave-test.163yun.com")
public interface ExtensionUserClient {

    @GetMapping("/rest/hello")
    String hello();
}
