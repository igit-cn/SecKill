package com.cloudSeckill.config;

import com.cloudSeckill.controller.ReceiveDataControllerDll;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
@EnableCaching
public class ReceiverDataConfig extends Thread{

    @Autowired private ReceiveDataControllerDll receiveDataController;
    @Autowired private UserMapper userMapper;

    @Override
    public void run() {
        //数据恢复
        dataRecover();
    }
    
    private void dataRecover(){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andOnlineStatusEqualTo(1);
        List<User> users = userMapper.selectByExample(userExample);
        for (User user : users) {
            receiveDataController.addToken(user);
        }
    }
    
    @PostConstruct
    public void createServerBootstrap() {
        start();
    }

    @PreDestroy
    public void stopServer() {
        
    }
}
