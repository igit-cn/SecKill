package com.cloudSeckill.config;

import com.cloudSeckill.net.http.HttpClient;
import com.cloudSeckill.service.URLGetJson.URLGetContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {
    @Autowired
    private IpAddressConfig ipAddressConfig;
    @Value("${local.ip}")
    private String local_ip;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        for (String ip : ipAddressConfig.getAllIP()) {
//            HttpClient httpClient = new HttpClient();
//            httpClient.setUrl(URLGetContent.getFullUrl(ip, URLGetContent.WXSetRecvMsgCallBack));
//            httpClient.addParams("url", "http://" + local_ip + "/receive/notification");
//            httpClient.sendAsJson(null);
//        }
    }
}
