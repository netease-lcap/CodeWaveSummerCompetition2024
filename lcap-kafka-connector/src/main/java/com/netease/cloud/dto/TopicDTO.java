package com.netease.cloud.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.cloud.KafkaConnector;

import java.util.List;


public class TopicDTO {
    public String name;
    /**
     * 分区数量
     */
    public Integer partitionsCount;
    /**
     * 副本数量
     */
    public Integer replicaCount;
    /**
     * 总数据大小，单位Bytes
     */
    public Long totalLogSize;

    /**
     * 节点
     *
     * @return
     */
    public List<BrokerDTO> brokers;

    /**
     * 消费者组
     *
     * @return
     */
    public List<String> consumerGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPartitionsCount() {
        return partitionsCount;
    }

    public void setPartitionsCount(Integer partitionsCount) {
        this.partitionsCount = partitionsCount;
    }

    public Integer getReplicaCount() {
        return replicaCount;
    }

    public void setReplicaCount(Integer replicaCount) {
        this.replicaCount = replicaCount;
    }

    public Long getTotalLogSize() {
        return totalLogSize;
    }

    public void setTotalLogSize(Long totalLogSize) {
        this.totalLogSize = totalLogSize;
    }

    public List<BrokerDTO> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<BrokerDTO> brokers) {
        this.brokers = brokers;
    }

    public List<String> getConsumerGroups() {
        return consumerGroups;
    }

    public void setConsumerGroups(List<String> consumerGroups) {
        this.consumerGroups = consumerGroups;
    }

    @Override
    public String toString() {
        try {
            return KafkaConnector.objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
