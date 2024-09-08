package com.fdddf.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class RSendCallback implements SendCallback {

    private final Function<Boolean, Boolean> callback;
    private final String msg;

    private static final Logger logger = LoggerFactory.getLogger(RSendCallback.class);

    public RSendCallback(String msg, Function<Boolean, Boolean> callback) {
        this.callback = callback;
        this.msg = msg;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        logger.info("异步消息 {} 发送成功 SendStatus = {}", msg, sendResult.getSendStatus());
        if (callback != null) {
            callback.apply(true);
        }
    }

    @Override
    public void onException(Throwable throwable) {
        logger.info("异步消息 {} 发送异常，exception = {}", msg, throwable.getMessage());
        if (callback != null) {
            callback.apply(false);
        }
    }
}
