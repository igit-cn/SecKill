package com.cloudSeckill.config;

import com.cloudSeckill.service.WechatServiceDllApi;
import com.cloudSeckill.service.WechatServiceInter;
import com.cloudSeckill.service.WechatServiceJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class WechatServiceConfig {
    @Bean
    @Scope("prototype")
//    @Primary
    public WechatServiceInter getWechatServiceInterDllApi() {
        return new WechatServiceDllApi();
    }

    @Bean
    @Scope("prototype")
    @Primary
    public WechatServiceInter getWechatServiceInter() {
        return new WechatServiceJson();
    }
}
