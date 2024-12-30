package com.netease.lowcode.custonapifilter.filter;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomFilterConfig {
    /**
     * 防重放过滤扩展地址列表，基础：/api/lcplogics。多个地址用逗号分隔。地址为右模糊匹配
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "/api/lcplogics"),
            @Environment(type = EnvironmentType.ONLINE, value = "/api/lcplogics")})
    @Value("${filterUrlList}")
    public String filterUrlList;
    /**
     * 过滤类型。默认为黑名单过滤，可选值：black、white
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "black"),
            @Environment(type = EnvironmentType.ONLINE, value = "black")})
    @Value("${filterType}")
    public String filterType;

    public String getFilterUrlList() {
        return filterUrlList;
    }

    public void setFilterUrlList(String filterUrlList) {
        this.filterUrlList = filterUrlList;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

}
