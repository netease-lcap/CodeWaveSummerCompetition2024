package com.fdddf.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Order
public class LibRocketMQEnvironmentMapperPostProcessor implements EnvironmentPostProcessor {
    private static final Map<String, String> ROCKETMQ_CONFIG_MAPPER = new HashMap<>();

    static {
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.producerGroup", "rocketmq.producer.group");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.producerAccessKey", "rocketmq.producer.access-key");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.producerSecretKey", "rocketmq.producer.secret-key");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.consumerGroup", "rocketmq.consumer.group");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.consumerTopic", "rocketmq.consumer.topic");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.consumerAccessKey", "rocketmq.consumer.access-key");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.consumerSecretKey", "rocketmq.consumer.secret-key");
        ROCKETMQ_CONFIG_MAPPER.put("extensions.RocketMQ.custom.nameServer", "rocketmq.name-server");
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> mapperProperties = new HashMap<>();
        ROCKETMQ_CONFIG_MAPPER.forEach((key, value) -> {
            if (environment.containsProperty(value)) {
                return;
            }
            if (environment.containsProperty(key)) {
                String property = environment.getProperty(key);
                if (!StringUtils.isEmpty(property)) {
                    mapperProperties.put(value, property);
                }
            }
        });

        if (mapperProperties.isEmpty()) {
            return;
        }
        environment.getPropertySources().addLast(new MapPropertySource("ROCKETMQ_CONFIG_MAPPER", mapperProperties));

    }
}
