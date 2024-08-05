package com.netease.lowcode.extension;

import com.netease.lowcode.extension.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {UserService.class})
public class OpenFeignAutoScan {
}
