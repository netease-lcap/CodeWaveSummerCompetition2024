package com.fdddf.rocketmq;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = LibraryAutoScan.class)
public class LibRocketMQSpringEnvironmentConfiguration {
    public LibRocketMQSpringEnvironmentConfiguration()
    {
        System.out.println("LibRocketMQSpringEnvironmentConfiguration");
    }
}
