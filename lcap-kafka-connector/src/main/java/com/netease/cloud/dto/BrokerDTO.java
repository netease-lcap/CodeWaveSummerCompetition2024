package com.netease.cloud.dto;

import java.util.ArrayList;
import java.util.List;

public class BrokerDTO {
    /**
     * broker id
     */
    public Long id;
    /**
     * broker host
     */
    public String host;
    /**
     * broker port
     */
    public Long port;
    /**
     * leader分区
     */
    public List<Long> leaderPartitions = new ArrayList<>();
    /**
     * follower分区
     */
    public List<Long> followerPartitions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public List<Long> getLeaderPartitions() {
        return leaderPartitions;
    }

    public void setLeaderPartitions(List<Long> leaderPartitions) {
        this.leaderPartitions = leaderPartitions;
    }

    public List<Long> getFollowerPartitions() {
        return followerPartitions;
    }

    public void setFollowerPartitions(List<Long> followerPartitions) {
        this.followerPartitions = followerPartitions;
    }
}