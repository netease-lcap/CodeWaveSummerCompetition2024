package com.fdddf.rocketmq;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class RocketMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerService.class);

    private final List<DefaultMQProducer> mqProducers = new ArrayList<>();

    @Resource
    private RocketMQConfig cfg;

    public RocketMQProducerService() {

    }

    private static RPCHook getAclRPCHook(String ak, String sk) {
        return new AclClientRPCHook(new SessionCredentials(ak, sk));
    }

    private DefaultMQProducer getMQProducer(String group, String topic) {
        for (DefaultMQProducer producer : mqProducers) {
            if (producer.getProducerGroup().equals(group) && producer.getCreateTopicKey().equals(topic)) {
                return producer;
            }
        }
        logger.info("add producer, group = {}, topic = {}", group, topic);
        RPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(cfg.accessKey, cfg.secretKey));
        DefaultMQProducer producer = new DefaultMQProducer(group, rpcHook);
        producer.setNamesrvAddr(cfg.nameServer);
        try {
            producer.start();
            mqProducers.add(producer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start RocketMQ producer", e);
        }
        return producer;
    }

    private Message buildMessage(String topic, String tag, String message) {
        try {
            return new Message(
                    topic,
                    tag,
                    message.getBytes(RemotingHelper.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to build RocketMQ message", e);
        }
    }


    /**
     * 发送普通消息
     *
     * @param group 群组ID，标识消息发送者所属的群组
     * @param topic 主题，标识消息的类型
     * @param tag 标签，用于对消息进行分类
     * @param msg 消息内容
     * @return
     */
    @NaslLogic
    public Boolean sendMessage(String group, String topic, String tag, String msg) {
        DefaultMQProducer producer = getMQProducer(group, topic);
        Message message = buildMessage(topic, tag, msg);
        try {
            SendResult sendResult = producer.send(message);
            return sendResult.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to send RocketMQ message, message = {}", msg);
        }
        return false;
    }

    /**
     * 发送普通消息（带超时时间）
     *
     * @param group 群组ID，标识消息发送者所属的群组
     * @param topic 主题，标识消息的类型
     * @param tag 标签，用于对消息进行分类
     * @param msg 消息内容
     * @param timeout 超时时间
     * @return
     */
    @NaslLogic
    public Boolean sendMessageTimeout(String group, String topic, String tag, String msg, Long timeout) {
        DefaultMQProducer producer = getMQProducer(group, topic);
        Message message = buildMessage(topic, tag, msg);
        try {
            SendResult sendResult = producer.send(message, timeout);
            return sendResult.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to send RocketMQ message, message = {}", msg);
        }
        return false;
    }

    /**
     * 发送普通消息（异步发送）
     *
     * @param group 群组ID，标识消息发送者所属的群组
     * @param topic 主题，标识消息的类型
     * @param tag 标签，用于对消息进行分类
     * @param msg 消息内容
     * @param callback 回调函数，接收一个布尔值参数，用于处理消息发送是否成功的结果
     */
//    @NaslLogic
//    public Boolean sendMessageCallback(String group, String topic, String tag, String msg, Function<Boolean, String> callback) {
//        DefaultMQProducer producer = getMQProducer(group, topic);
//        Message message = buildMessage(topic, tag, msg);
//        try {
//            producer.send(message, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    callback.apply(sendResult.getSendStatus() == SendStatus.SEND_OK);
//                }
//                @Override
//                public void onException(Throwable e) {
//                    callback.apply(false);
//                    logger.error("Failed to send RocketMQ message, message = {}", msg);
//                    e.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("Failed to send RocketMQ message, message = {}", msg);
//        }
//        return true;
//    }

    /**
     * 发送普通消息（异步发送，带超时时间）
     *
     * @param group 群组ID，标识消息发送者所属的群组
     * @param topic 主题，标识消息的类型
     * @param tag 标签，用于对消息进行分类
     * @param msg 消息内容
     * @param timeout 超时时间
     * @param callback 回调函数，接收一个布尔值参数，用于处理消息发送是否成功的结果
     */
//    @NaslLogic
//    public Boolean sendMessageCallbackTimeout(String group, String topic, String tag, String msg, Long timeout,
//                                              Function<Boolean, String> callback) {
//        DefaultMQProducer producer = getMQProducer(group, topic);
//        Message message = buildMessage(topic, tag, msg);
//        try {
//            producer.send(message, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    callback.apply(sendResult.getSendStatus() == SendStatus.SEND_OK);
//                }
//                @Override
//                public void onException(Throwable e) {
//                    callback.apply(false);
//                    logger.error("Failed to send RocketMQ message, message = {}", msg);
//                    e.printStackTrace();
//                }
//            }, timeout);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("Failed to send RocketMQ message, message = {}", msg);
//        }
//        return true;
//    }

    /**
     * 发送普通消息（单向发送）
     *
     * @param group 群组ID，标识消息发送者所属的群组
     * @param topic 主题，标识消息的类型
     * @param tag 标签，用于对消息进行分类
     * @param msg 消息内容
     */
    @NaslLogic
    public Boolean sendOneWay(String group, String topic, String tag, String msg) {
        DefaultMQProducer producer = getMQProducer(group, topic);
        Message message = buildMessage(topic, tag, msg);
        try {
            producer.sendOneway(message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to send RocketMQ message, message = {}", msg);
        }
        return true;
    }

    private void shutdown() {
        for (DefaultMQProducer producer : mqProducers) {
            producer.shutdown();
        }
    }

}
