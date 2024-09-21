package com.fdddf.websocket.service;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Register {
    private static Function<CommonRequestMessage, CommonReplyMessage> messageHandler;

    @NaslLogic
    public Boolean registerMessageHandler(Function<CommonRequestMessage, CommonReplyMessage> messageHandler)
    {
        Register.messageHandler = messageHandler;
        return true;
    }

    public static Function<CommonRequestMessage, CommonReplyMessage> getMessageHandler() {
        return messageHandler;
    }
}
