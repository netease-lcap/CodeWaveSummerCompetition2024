package com.netease.lowcode.xxljob.spring;

import com.netease.lowcode.xxljob.LibraryAutoScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 加入spring环境配置（在spring.factories中指定）
 */
@Configuration
@ComponentScan(basePackageClasses = LibraryAutoScan.class)
public class XxlJobSpringEnvironmentConfiguration {
}
