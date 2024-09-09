package com.fdddf.rocketmq;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Function;

@Service
public class RocketMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerService.class);

    @Autowired
    private RocketMQTemplate template;

    @Resource
    private RocketMQConfig cfg;

    /**
     * 发送普通消息
     * @param msg 消息体
     * @return true
     */
    @NaslLogic
    public Boolean sendMessage(String msg) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        template.convertAndSend(cfg.consumerTopic, msg);
        logger.info("普通消息发送完成：message = {}", msg);
        return true;
    }

    /**
     * 发送同步消息
     *
     * @param msg 消息体
     */
    @NaslLogic
    public Boolean syncSendMessage(String msg) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        SendResult sendResult = template.syncSend(cfg.consumerTopic, msg);
        logger.info("同步发送消息完成：message = {}, sendResult = {}", msg, sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    /**
     * 发送异步消息
     *
     * @param msg 消息体
     */
    @NaslLogic
    public Boolean asyncSendMessage(String msg, Function<Boolean, Boolean> callback) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        RSendCallback sendCallback = new RSendCallback(msg, callback);
        template.asyncSend(cfg.consumerTopic, msg, sendCallback);
        return true;
    }

    /**
     * 发送单向消息
     *
     * @param msg 消息体
     */
    @NaslLogic
    public Boolean sendOneWayMessage(String msg) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        template.sendOneWay(cfg.consumerTopic, msg);
        logger.info("单向发送消息完成：message = {}", msg);
        return true;
    }

    /**
     * 发送携带 tag 的消息（过滤消息）
     *
     * @param tag tag
     * @param msg 消息体
     */
    @NaslLogic
    public Boolean syncSendMessageWithTag(String tag, String msg) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        if (tag == null) {
            logger.error("Tag is null");
            throw new RException("Tag is null");
        }
        String topic = cfg.consumerTopic + ":" + tag;
        template.syncSend(topic, msg);
        logger.info("发送带 tag 的消息完成：message = {}", msg);
        return true;
    }

    /**
     * 同步发送延时消息
     *
     * @param msg    消息体
     * @param timeout    超时
     * @param delayLevel 延时等级：现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，
     *                   从1s到2h分别对应着等级 1 到 18，消息消费失败会进入延时消息队列
     *                   "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     */
    @NaslLogic
    public Boolean syncSendDelay(String msg, Long timeout, Integer delayLevel) {
        validateDelayMsg(msg, timeout, delayLevel);
        template.syncSend(cfg.consumerTopic, MessageBuilder.withPayload(msg).build(), timeout, delayLevel);
        logger.info("已同步发送延时消息 message = {}", msg);
        return true;
    }

    /**
     * 异步发送延时消息
     *
     * @param msg    消息对象
     * @param timeout    超时时间
     * @param delayLevel 延时等级：现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，
     *                   从1s到2h分别对应着等级 1 到 18，消息消费失败会进入延时消息队列
     *                   "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     * @param callback 回调函数
     */
    @NaslLogic
    public Boolean asyncSendDelay(String msg, Long timeout, Integer delayLevel, Function<Boolean, Boolean> callback) {
        validateDelayMsg(msg, timeout, delayLevel);
        RSendCallback sendCallback = new RSendCallback(msg, callback);
        template.asyncSend(cfg.consumerTopic, MessageBuilder.withPayload(msg).build(), sendCallback, timeout, delayLevel);
        logger.info("已异步发送延时消息 message = {}", msg);
        return true;
    }

    private void validateDelayMsg(String msg, Long timeout, Integer delayLevel) {
        if (msg == null) {
            logger.error("Message is null");
            throw new RException("Message is null");
        }
        if (timeout == null) {
            logger.error("Timeout is null");
            throw new RException("Timeout is null");
        }
        if (delayLevel == null) {
            logger.error("Delay level is null");
            throw new RException("Delay level is null");
        }
        // Ensure the delay level is valid
        if (delayLevel < 1 || delayLevel > 18) {
            logger.error("Invalid delay level: {}", delayLevel);
            throw new RException("Invalid delay level: " + delayLevel);
        }
    }
}
