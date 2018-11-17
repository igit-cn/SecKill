package com.cloudSeckill.config;

import com.cloudSeckill.net.http.HttpClient;
import com.cloudSeckill.service.URLGetJson.URLGetContent;
import com.cloudSeckill.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {
//    @Autowired
//    private IpAddressConfig ipAddressConfig;
//    @Value("${local.ip}")
//    private String local_ip;
//
//    @Value("${local.ip_outer}")
//    private String local_ip_outer;
//
//    @Value("${three.ip4}")
//    private String ip_outer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //        DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
//        for (String ip : ipAddressConfig.getAllIP()) {
//            HttpClient httpClient = new HttpClient();
//            httpClient.setUrl(URLGetContent.getFullUrl(ip, URLGetContent.WXSetRecvMsgCallBack));
//            httpClient.addParams("url", "http://" + (TextUtils.equals(ip, ip_outer) ? local_ip_outer : local_ip) + "/receive/notification");
//            httpClient.sendAsJson(null);
//        }
    }
}
