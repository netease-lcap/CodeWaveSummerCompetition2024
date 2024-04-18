package com.wgx.spring;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

//用于扫描com.wgx包
@Configurable
@ComponentScan(basePackages = "com.wgx")
public class SpringEnvironmentConfiguration {
}
