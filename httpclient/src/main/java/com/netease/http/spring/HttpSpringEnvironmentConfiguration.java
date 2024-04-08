package com.netease.http.spring;

import com.netease.http.LibraryAutoScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = LibraryAutoScan.class)
public class HttpSpringEnvironmentConfiguration {
}
