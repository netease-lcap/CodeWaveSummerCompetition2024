package com.fdddf.websocket.service;

import com.fdddf.websocket.WebsocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

@Component
@ServerEndpoint(value = "/webSocket/{uid}")
public class WebSocketServer {

    private static Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private static int onlineCount = 0;

    private Long uid;

    private Session session;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") Long uid) {
        this.session = session;
        this.uid = uid;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("有新连接加入！当前在线人数为：{}", getOnlineCount());
    }

    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);
        subOnlineCount();
        logger.info("释放的uid："+uid);
        logger.info("有连接断开！当前在线人数为：{}", getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("收到来自UID:" + uid + "的信息:" + message);

        sendMessage(message);
    }

    public void sendMessage(String message) throws IOException {
        CommonRequestMessage msg = new CommonRequestMessage(message, uid);
        Function<CommonRequestMessage, CommonReplyMessage> handler = Register.getMessageHandler();
        CommonReplyMessage reply;
        if (handler == null) {
            reply = new CommonReplyMessage("No handler found to process message: "+ message, uid);
        } else {
            reply = Register.getMessageHandler().apply(msg);
        }
        try {
            this.session.getBasicRemote().sendText(reply.content);
        } catch (IOException e) {
            throw new WebsocketException(e);
        }
    }

    public void broadcast(String message) throws IOException {
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendToUser(String message, @PathParam("uid") Long uid) {
        logger.info("推送消息到UID:" + uid + "，推送内容:" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个uid的，为null则全部推送
                if (uid == null) {
                    item.sendMessage(message);
                } else if (Objects.equals(item.uid, uid)) {
                    item.sendMessage(message);
                }
            } catch (IOException ignored) {
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("发生错误：{}", throwable.getMessage());
        throwable.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }
}
