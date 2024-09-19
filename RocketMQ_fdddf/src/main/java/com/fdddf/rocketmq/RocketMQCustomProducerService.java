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
public class RocketMQCustomProducerService {
    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerService.class);

    @Autowired
    private RocketMQTemplate template;

    @Resource
    private RocketMQConfig cfg;

    /**
     * 发送普通消息
     * @param topic topic
     * @param msg 消息体
     * @param group group
     * @return true
     */
    @NaslLogic
    public Boolean sendTopicMessage(String topic, String msg, String group) {
        if (msg == null || topic == null) {
            logger.error("Message or topic is null");
            throw new RException("Message or topic is null");
        }
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        template.convertAndSend(topic, msg);
        logger.info("普通消息发送完成： topic={} message = {}", topic, msg);
        return true;
    }

    /**
     * 发送同步消息
     *
     * @param topic topic
     * @param msg 消息体
     * @param group group
     */
    @NaslLogic
    public Boolean syncSendTopicMessage(String topic, String msg, String group) {
        if (msg == null || topic == null) {
            logger.error("Message or topic is null");
            throw new RException("Message or topic is null");
        }
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        SendResult sendResult = template.syncSend(topic, msg);
        logger.info("同步发送消息完成：topic={} message = {}, sendResult = {}", topic, msg, sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    /**
     * 发送异步消息
     *
     * @param topic topic
     * @param msg 消息体
     * @param callback 回调
     * @param group group
     */
    @NaslLogic
    public Boolean asyncSendTopicMessage(String topic, String msg, Function<Boolean, Boolean> callback, String group) {
        if (msg == null || topic == null) {
            logger.error("Message or topic is null");
            throw new RException("Message or topic is null");
        }
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        RSendCallback sendCallback = new RSendCallback(msg, callback);
        template.asyncSend(topic, msg, sendCallback);
        return true;
    }

    /**
     * 发送单向消息
     *
     * @param topic topic
     * @param msg 消息体
     * @param group group
     */
    @NaslLogic
    public Boolean sendOneWayTopicMessage(String topic, String msg, String group) {
        if (msg == null || topic == null) {
            logger.error("Message or topic is null");
            throw new RException("Message or topic is null");
        }
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        template.sendOneWay(topic, msg);
        logger.info("单向发送消息完成：topic={} message = {}", topic, msg);
        return true;
    }

    /**
     * 发送携带 tag 的消息（过滤消息）
     *
     * @param topic topic
     * @param tag tag
     * @param msg 消息体
     * @param group group
     */
    @NaslLogic
    public Boolean syncSendTopicMessageWithTag(String topic, String tag, String msg, String group) {
        if (msg == null || topic == null) {
            logger.error("Message or topic is null");
            throw new RException("Message or topic is null");
        }
        if (tag == null) {
            logger.error("Tag is null");
            throw new RException("Tag is null");
        }
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        String finalTopic = topic + ":" + tag;
        template.syncSend(finalTopic, msg);
        logger.info("发送带 tag 的消息完成：topic={} message = {}", finalTopic, msg);
        return true;
    }

    /**
     * 同步发送延时消息
     *
     * @param topic    topic
     * @param msg    消息体
     * @param timeout    超时
     * @param delayLevel 延时等级：现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，
     *                   从1s到2h分别对应着等级 1 到 18，消息消费失败会进入延时消息队列
     *                   "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     * @param group group
     */
    @NaslLogic
    public Boolean syncSendTopicDelay(String topic, String msg, Long timeout, Integer delayLevel, String group) {
        validateDelayMsg(topic, msg, timeout, delayLevel);
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        template.syncSend(topic, MessageBuilder.withPayload(msg).build(), timeout, delayLevel);
        logger.info("已同步发送延时消息 topic={} message = {}", topic, msg);
        return true;
    }

    /**
     * 异步发送延时消息
     *
     * @param topic    topic
     * @param msg    消息对象
     * @param timeout    超时时间
     * @param delayLevel 延时等级：现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，
     *                   从1s到2h分别对应着等级 1 到 18，消息消费失败会进入延时消息队列
     *                   "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     * @param callback 回调
     * @param group group
     */
    @NaslLogic
    public Boolean asyncSendTopicDelay(String topic, String msg, Long timeout, Integer delayLevel,
                                       Function<Boolean, Boolean> callback, String group) {
        validateDelayMsg(topic, msg, timeout, delayLevel);
        RSendCallback sendCallback = new RSendCallback(msg, callback);
        if (group != null) {
            template.getProducer().setProducerGroup(group);
        }
        template.asyncSend(topic, MessageBuilder.withPayload(msg).build(), sendCallback, timeout, delayLevel);
        logger.info("已异步发送延时消息 topic={} message = {}", topic, msg);
        return true;
    }

    private void validateDelayMsg(String topic, String msg, Long timeout, Integer delayLevel) {
        if (topic == null) {
            logger.error("Topic is null");
            throw new RException("Topic is null");
        }
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
