package com.fdddf.rocketmq;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class Consumer {

    public String group;
    public String topic;

    public Consumer()
    {
    }

    public Consumer(String group, String topic)
    {
        this.group = group;
        this.topic = topic;
    }
}
