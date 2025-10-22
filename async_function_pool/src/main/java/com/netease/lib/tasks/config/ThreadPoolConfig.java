package com.netease.lib.tasks.config;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("libraryThreadPoolConfig")
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    @Value("${corePoolSize:8}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "8"),
            @Environment(type = EnvironmentType.ONLINE, value = "8")
    })
    public String corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${maxPoolSize:24}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "24"),
            @Environment(type = EnvironmentType.ONLINE, value = "24")
    })
    public String maxPoolSize;

    /**
     * 队列大小
     */
    @Value("${queueCapacity:1024}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "1024"),
            @Environment(type = EnvironmentType.ONLINE, value = "1024")
    })
    public String queueCapacity;

    /**
     * 核心线程等待销毁时间
     */
    @Value("${keepAliveSeconds:60}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "60"),
            @Environment(type = EnvironmentType.ONLINE, value = "60")
    })
    public String keepAliveSeconds;

    public String getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(String corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public String getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(String maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(String queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(String keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
