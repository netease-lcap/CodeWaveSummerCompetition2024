package com.fdddf.rocketmq;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NaslStructure
public class RocketMQConfig {

    /**
     * rocketmq name server
     */
    @Value("${rocketmq.name-server}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String nameServer;

    /**
     * rocketmq producer group
     */
    @Value("${rocketmq.producer.group}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String producerGroup;

    /**
     * rocketmq accessKey for producer
     */
    @Value("${rocketmq.producer.access-key}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String producerAccessKey;

    /**
     * rocketmq secretKey for producer
     */
    @Value("${rocketmq.producer.secret-key}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String producerSecretKey;


    /**
     * rocketmq consumer group
     */
    @Value("${rocketmq.consumer.group}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String consumerGroup;

    /**
     * rocketmq consumer topic
     */
    @Value("${rocketmq.consumer.topic}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "test"))
    public String consumerTopic;

    /**
     * rocketmq accessKey for consumer
     */
    @Value("${rocketmq.consumer.access-key}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String consumerAccessKey;

    /**
     * rocketmq secretKey for consumer
     */
    @Value("${rocketmq.consumer.secret-key}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String consumerSecretKey;

}
