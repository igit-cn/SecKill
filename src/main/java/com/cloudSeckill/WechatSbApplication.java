package com.cloudSeckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"com"})
public class WechatSbApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WechatSbApplication.class, args);
    }
}
