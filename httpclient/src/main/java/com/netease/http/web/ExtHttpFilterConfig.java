package com.netease.http.web;

import com.netease.http.web.filter.ExtHttpLogicAuthFilter;
import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class ExtHttpFilterConfig {
    /**
     * 是否开启扩展登录鉴权，0：关闭，1：开启登录验证，2：使用自定义鉴权，3：使用登录+自定义鉴权
     */
    @Value("${expandLogicAuthFlag}")
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "0"),
            @Environment(type = EnvironmentType.ONLINE, value = "0")})
    private String expandLogicAuthFlag;

    /**
     * lcap auth的secret
     */
    @Value("${auth.token.secret}")
    private volatile String secret;

    @Bean
    public FilterRegistrationBean<Filter> expandTransferLogicAuthFilterRegistration() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        ExtHttpLogicAuthFilter extHttpLogicAuthFilter = new ExtHttpLogicAuthFilter();
        extHttpLogicAuthFilter.setExpandLogicAuthFlag(expandLogicAuthFlag);
        extHttpLogicAuthFilter.setSecret(secret);
        registrationBean.setFilter(extHttpLogicAuthFilter);
        registrationBean.addUrlPatterns("/expand/file/*");
        return registrationBean;
    }

}