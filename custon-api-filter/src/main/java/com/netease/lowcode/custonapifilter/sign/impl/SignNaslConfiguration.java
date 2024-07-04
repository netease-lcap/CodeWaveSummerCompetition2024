package com.netease.lowcode.custonapifilter.sign.impl;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import com.netease.lowcode.core.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 签名环境变量配置类
 */
@Component
@NaslStructure
public class SignNaslConfiguration {
    /**
     * 与前端配套使用的公钥
     */
    @Value("${secretKey}")
    @NaslConfiguration
    @Required
    public String secretKey;
    /**
     * 防重放最大有效时间，单位ms
     */
    @Value("${signMaxTime}")
    @Required
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "60000"), @Environment(type = EnvironmentType.ONLINE, value = "60000")})
    public String signMaxTime;

}
