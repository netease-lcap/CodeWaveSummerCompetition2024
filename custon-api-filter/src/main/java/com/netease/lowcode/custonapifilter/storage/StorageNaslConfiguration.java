package com.netease.lowcode.custonapifilter.storage;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 存储工具环境变量配置类
 */
@Component
@NaslStructure
public class StorageNaslConfiguration {

    /**
     * 数据存储策略。redis/local/db
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "redis"),
            @Environment(type = EnvironmentType.ONLINE, value = "redis")})
    @Value("${storageStrategy}")
    public String storageStrategy;

    /**
     * 算法策略。rsa/des/aes
     */
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "rsa"),
            @Environment(type = EnvironmentType.ONLINE, value = "rsa")})
    @Value("${signatureStrategy}")
    public String signatureStrategy;

    public String getStorageStrategy() {
        return storageStrategy;
    }

    public void setStorageStrategy(String storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    public String getSignatureStrategy() {
        return signatureStrategy;
    }

    public void setSignatureStrategy(String signatureStrategy) {
        this.signatureStrategy = signatureStrategy;
    }
}
