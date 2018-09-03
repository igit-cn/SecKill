package com.cloudSeckill.net.web_socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WechatWebSocket extends BaseWebSocket {

    /**
     * 接收web端回传消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("你将收到的离线"));
    }
}
