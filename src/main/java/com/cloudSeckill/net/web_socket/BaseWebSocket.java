package com.cloudSeckill.net.web_socket;

import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.utils.LogUtils;
import com.cloudSeckill.utils.SessionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.LinkedList;

public class BaseWebSocket extends TextWebSocketHandler {
    
    protected final LinkedList<WebSocketSession> users = new LinkedList();

    /**
     * 连接成功时候，会触发Web端onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
        LogUtils.info(SessionUtils.getUserInfoFromWebSocket(session).userName + "建立WebSocket连接成功!");
    }

    /**
     * 给某个用户发送消息
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession user : users) {
            UserInfo userInfo = SessionUtils.getUserInfoFromWebSocket(user);
            if (userInfo != null && userInfo.userName.equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                        LogUtils.info("向用户 " + userName + " 发送数据 :" + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 给所有在线用户发送消息
     */
    public void sendMessageToAllUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给某个前端发送消息
     */
    public void sendMessageToSessionId(String sessionId, TextMessage message) {
        for (WebSocketSession user : users) {
            if (sessionId != null && sessionId.equals(SessionUtils.getSessionIdFromWebSocketSession(user))) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                        LogUtils.info("向用户session " + sessionId + ",用户名 : " + SessionUtils.getUserInfoFromWebSocket(user).userName + " 发送数据 :" + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LogUtils.info("webSocket移除 : " + SessionUtils.getUserInfoFromWebSocket(session).userName);
        users.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
