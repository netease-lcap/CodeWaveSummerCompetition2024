package com.fdddf.websocket.service;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class CommonReplyMessage {
    public String content;

    public Long userId;

    public CommonReplyMessage() {}

    public CommonReplyMessage(String content, Long userId)
    {
        this.content = content;
        this.userId = userId;
    }
}

