package com.netease.lowcode.lib.filter;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomUrlConfig {
    /**
     * 拦截地址列表，基础：。。。多个地址用逗号分隔。地址为右模糊匹配
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "/upload/download_files"),
            @Environment(type = EnvironmentType.ONLINE, value = "/upload/download_files")})
    @Value("${filterUrlList}")
    public String filterUrlList;

    public String getFilterUrlList() {
        return filterUrlList;
    }

    public void setFilterUrlList(String filterUrlList) {
        this.filterUrlList = filterUrlList;
    }

}
