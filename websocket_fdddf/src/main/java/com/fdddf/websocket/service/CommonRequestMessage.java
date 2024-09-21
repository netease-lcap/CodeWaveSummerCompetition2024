package com.fdddf.websocket.service;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class CommonRequestMessage {
    public String content;

    public Long userId;

    public CommonRequestMessage() {}

    public CommonRequestMessage(String message, long userId) {
        this.content = message;
        this.userId = userId;
    }
}
