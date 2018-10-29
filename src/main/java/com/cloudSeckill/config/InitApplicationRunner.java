package com.cloudSeckill.config;

import com.cloudSeckill.service.DllInterface;
import com.cloudSeckill.utils.LogUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args){
        LogUtils.info("WXSetNetworkVerifyInfo开始");
        DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
        LogUtils.info("WXSetNetworkVerifyInfo结束");
    }
}
