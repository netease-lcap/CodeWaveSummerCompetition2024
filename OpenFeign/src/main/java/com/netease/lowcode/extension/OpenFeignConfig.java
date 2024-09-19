package com.netease.lowcode.extension;

import com.netease.lowcode.extension.feign.ExtensionUserClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = ExtensionUserClient.class)
public class OpenFeignConfig {

    public OpenFeignConfig() {
        System.out.println("OpenFeignConfig 扫描成功");
    }
}
