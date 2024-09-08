package com.netease.lowcode.custonapifilter.filter;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {
    /**
     * 防重放过滤地址列表，多个地址用逗号分隔。地址为右模糊匹配
     */
    @NaslConfiguration
    @Value("${filterUrlList}")
    public String filterUrlList;

}
