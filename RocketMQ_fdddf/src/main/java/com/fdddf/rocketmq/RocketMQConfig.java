package com.fdddf.rocketmq;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NaslStructure
public class RocketMQConfig {

    /**
     * rocketmq name server
     */
    @Value("${rocketmq.nameServer}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String nameServer;

    /**
     * rocketmq group
     */
    @Value("${rocketmq.group}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String group;

    /**
     * rocketmq topic
     */
    @Value("${rocketmq.topic}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String topic;

    /**
     * rocketmq tag
     */
    @Value("${rocketmq.tag}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String tag;

    /**
     * rocketmq accessKey
     */
    @Value("${rocketmq.accessKey}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String accessKey;

    /**
     * rocketmq secretKey
     */
    @Value("${rocketmq.secretKey}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String secretKey;
}
