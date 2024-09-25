package com.netease.cloud;

public class RedisTest {

    public static void main(String[] args) {
        testConnection();
        testGetSetValue();
    }

    private static void testConnection(){
        RedisConnector redisConnector = new RedisConnector();
        System.out.println(redisConnector.testConnection("127.0.0.1", 39383,"",0));
    }

     private static void testGetSetValue(){
        RedisConnector redisConnector = new RedisConnector();
        redisConnector.initRedisTemplate("127.0.0.1", 39383, "", 0);
        redisConnector.setValue("jtsKey","jtsValue");
        String jtsKey = redisConnector.getValue("jtsKey");
        System.out.println(jtsKey);
    }
}
