package com.fdddf.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.consumer.topic}", consumerGroup = "${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.consumer.tag}", selectorType = SelectorType.TAG)
public class TagMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {

    }
}
