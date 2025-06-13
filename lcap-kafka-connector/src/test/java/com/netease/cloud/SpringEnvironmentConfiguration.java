package com.netease.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackageClasses = SpringEnvironmentConfiguration.class)
public class SpringEnvironmentConfiguration {
    private static final Logger log = LoggerFactory.getLogger(KafkaTest.class);

    @Bean(name = "kafka1")
    public KafkaConnector kafka1() {
        return new KafkaConnector().initBean("47.98.102.217:9092", "SASL_PLAINTEXT", "PLAIN", "admin", "admin-secret");
    }

    @Bean(name = "kafka2")
    public KafkaConnector kafka2() {
        return new KafkaConnector().initBean("47.98.102.217:9093"," "," ", " ", " ");
    }

    @PostConstruct
    public void initKafka1() {
        kafka1().subscribe("topic1", "test", data -> {
            log.info("topic1收到消息:{}", data);
            return null;
        });
    }

    @PostConstruct
    public void initKafka2() {
        kafka2().subscribe("topic1", "test", data -> {
            log.info("topic1收到消息:{}", data);
            return null;
        });
    }
}
