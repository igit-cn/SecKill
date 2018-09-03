package com.cloudSeckill.config;

import com.cloudSeckill.net.web_socket.WechatWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired WechatWebSocket getWechatWebSocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWechatWebSocket,"/getQRCodeWebSocket").addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(getWechatWebSocket, "/sockjs/getQRCodeWebSocket").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
    }

}