package com.fdddf.websocket.controller;

import com.fdddf.websocket.service.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller("chat")
@RequestMapping("/chat")
public class ChatController {
    @ResponseBody
    @RequestMapping("/push/{uid}")
    public Map pushToWeb(@PathVariable Long uid, String message) {
        Map<String,Object> result = new HashMap<>();
        WebSocketServer.sendToUser(message, uid);
        result.put("uid", uid);
        result.put("message", message);
        return result;
    }
}
