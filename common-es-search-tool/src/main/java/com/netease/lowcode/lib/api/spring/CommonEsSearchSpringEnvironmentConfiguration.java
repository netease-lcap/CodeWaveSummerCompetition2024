package com.netease.lowcode.lib.api.spring;

import com.netease.lowcode.lib.api.LibraryAutoScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 加入spring环境配置（在spring.factories中指定）
 */
@Configuration
@ComponentScan(basePackageClasses = LibraryAutoScan.class)
public class CommonEsSearchSpringEnvironmentConfiguration {
    public CommonEsSearchSpringEnvironmentConfiguration() {
        System.out.println("EsSearchSpringEnvironmentConfiguration");
    }
}
