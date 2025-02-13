package com.netease.lowcode.lib.officetopdf.web;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.lib.officetopdf.web.filter.ExpandTransferLogicAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class OfficePdfFilterConfig {
    /**
     * 是否开启扩展登录鉴权，0：关闭，1：开启
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
        ExpandTransferLogicAuthFilter expandTransferLogicAuthFilter = new ExpandTransferLogicAuthFilter();
        expandTransferLogicAuthFilter.setExpandLogicAuthFlag(expandLogicAuthFlag);
        expandTransferLogicAuthFilter.setSecret(secret);
        registrationBean.setFilter(expandTransferLogicAuthFilter);
        registrationBean.addUrlPatterns("/expand/transfer/*");
        return registrationBean;
    }

}