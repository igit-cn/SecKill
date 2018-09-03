package com.cloudSeckill.config;

import com.cloudSeckill.net.socket.socket.SocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SocketConfig {
    
//    @Value("${three.ip}")
    private String ipAddress;
    
    private int port = 8889;
    
    @PostConstruct
    public void initSocket(){
//        SocketClient.init(ipAddress,port);
    }
    
}
