package com.netease.lowcode.dubbo.spring;

import com.netease.lowcode.dubbo.LibraryAutoScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 加入spring环境配置（在spring.factories中指定）
 */
@Configuration
@ComponentScan(basePackageClasses = LibraryAutoScan.class)
public class DubboStarterSpringEnvironmentConfiguration {
    public DubboStarterSpringEnvironmentConfiguration() {
        System.out.println("DubboStarterSpringEnvironmentConfiguration start");
    }
}
