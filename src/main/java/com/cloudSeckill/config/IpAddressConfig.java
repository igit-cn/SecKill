package com.cloudSeckill.config;

import com.cloudSeckill.controller.ReceiveDataController;
import com.opslab.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class IpAddressConfig {

    @Autowired
    private ReceiveDataController receiveDataController;

    @Value("${three.ip1}")
    private String ip1;
    @Value("${three.ip1.weight}")
    private String ip1Weight;

    @Value("${three.ip2}")
    private String ip2;
    @Value("${three.ip2.weight}")
    private String ip2Weight;

    @Value("${three.ip3}")
    private String ip3;
    @Value("${three.ip3.weight}")
    private String ip3Weight;

    @Value("${three.port}")
    private String port;

    public String getRandomIP() {
        Map<String, Integer> list = new HashMap();
        list.put(ip1, Integer.parseInt(ip1Weight));
        list.put(ip2, Integer.parseInt(ip2Weight));
        list.put(ip3, Integer.parseInt(ip3Weight));

        int allWeight = 0;
        for (String ip : list.keySet()) {
            allWeight += list.get(ip);
        }

        int currentWeight = 0;
        int index = RandomUtil.integer(0, allWeight);
        for (String ip : list.keySet()) {
            currentWeight += list.get(ip);
            if (index < currentWeight) {
                return ip;
            }
        }
        return "";
    }

    public String getPort() {
        return port;
    }

    public String[] getAllIP() {
        return new String[]{ip1, ip2, ip3};
    }
}
