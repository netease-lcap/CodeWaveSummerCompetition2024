package com.captcha.spring;

import com.captcha.LibraryAutoScan;
import com.captcha.config.RedisConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 加入spring环境配置（在spring.factories中指定）
 */
@Configuration
@ComponentScan(basePackageClasses = {LibraryAutoScan.class, RedisConfig.class})
public class LibCaptchaSpringEnvironmentConfiguration {
    public LibCaptchaSpringEnvironmentConfiguration() {
        System.out.println("LibCaptchaSpringEnvironmentConfiguration");
    }
}
