package com.fdddf.rocketmq;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class RocketMQConsumerService {

    private final List<DefaultMQPushConsumer> mqConsumers = new ArrayList<>();

    @Resource
    private RocketMQConfig cfg;

    /**
     * 注册消费者
     * @param consumers 消费者列表
     * @param processMessageFunction 消息回调逻辑
     * @return 消费者数量
     * @throws RException
     */
    @NaslLogic
    @PostConstruct
    public Integer register(List<Consumer> consumers, Function<String, String> processMessageFunction)
            throws RException {
        try {
            if (cfg.group != null && cfg.topic != null) {
                // check the default consumer if it's in the consumers
                for (int i = 0; i < consumers.size(); i++) {
                    if (consumers.get(i).group.equals(cfg.group) && consumers.get(i).topic.equals(cfg.topic)) {
                        consumers.remove(i);
                        break;
                    }
                }
                consumers.add(new Consumer(cfg.group, cfg.topic));
            }
            RPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(cfg.accessKey, cfg.secretKey));
            for (Consumer consumer : consumers) {
                DefaultMQPushConsumer theConsumer = new DefaultMQPushConsumer(consumer.group, rpcHook);
                theConsumer.setNamesrvAddr(cfg.nameServer);
                theConsumer.subscribe(consumer.topic, "*");
                theConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                    for (MessageExt msg : msgs) {
                        System.out.println("Received Message: " + msg);
                        processMessageFunction.apply(new String(msg.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                });
                theConsumer.start();
                mqConsumers.add(theConsumer);
            }
        } catch (Exception e) {
            throw new RException("Failed to register consumers", e);
        }
        return mqConsumers.size();
    }

    @PreDestroy
    public void shutdown() {
        for (DefaultMQPushConsumer consumer : mqConsumers) {
            consumer.shutdown();
        }
    }

}
