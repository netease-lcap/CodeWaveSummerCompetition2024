package com.netease.cloud;

import org.junit.Test;


public class KafkaTestConnection {
    /**
     * 连通性测试
     */

    @Test
    public void testConnection() {
        Boolean aBoolean = new KafkaConnector().testListTopics("47.98.102.217:9092", "SASL_PLAINTEXT", "PLAIN", "admin", "admin-secret");
        System.out.println("连通性测试结果 ：" + aBoolean);
    }
}
