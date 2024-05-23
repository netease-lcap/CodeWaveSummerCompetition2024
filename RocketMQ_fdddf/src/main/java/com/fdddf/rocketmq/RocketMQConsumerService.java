package com.fdddf.rocketmq;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RocketMQMessageListener(topic = "${rocketmq.consumer.topic}", consumerGroup = "${rocketmq.consumer.group}",
        accessKey = "${rocketmq.consumer.access-key}", secretKey = "${rocketmq.consumer.secret-key}")
public class RocketMQConsumerService implements RocketMQListener<String> {

    private Function<String, String> consumerCallback;

    /**
     * 设置消费回调
     * @param callback 消费回调
     * @return boolean
     */
    @NaslLogic
    public Boolean setConsumerCallback(Function<String, String> callback) {
        this.consumerCallback = callback;
        return true;
    }
    @Override
    public void onMessage(String message) {
        System.out.println("Received Message: " + message);
        consumerCallback.apply(message);
    }
}
