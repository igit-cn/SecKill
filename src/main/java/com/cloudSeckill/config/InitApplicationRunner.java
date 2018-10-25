package com.cloudSeckill.config;

import com.cloudSeckill.service.DllInterface;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args){
        DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
    }
}
