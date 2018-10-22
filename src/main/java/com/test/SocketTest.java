package com.test;

import com.cloudSeckill.net.socket.socket.SocketClient;
import com.cloudSeckill.service.URLGetJson.URLGetContent;
import com.cloudSeckill.utils.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

public class SocketTest {
    public static void main(String[] args) {
        SocketClient.init("47.92.166.84", 8888);
        SocketClient client = new SocketClient();
//        client.putParam("name", Base64.getEncoder().encodeToString("hahahahaipad".getBytes()));
//        client.putParam("mac", Base64.getEncoder().encodeToString(Utils.getRandomMac().getBytes()));
//        client.putParam("uuid", Base64.getEncoder().encodeToString(Utils.getRandomUUID().getBytes()));
//        client.putParam("method", Base64.getEncoder().encodeToString("WXInitialize".getBytes()));

        client.putParam("device_name", "hahahahaipad");
        client.putParam("device_type", Utils.getRandomMac());
        client.putParam("device_uuid", Utils.getRandomUUID());
        client.putParam("method", Base64.getEncoder().encodeToString("WXInitialize".getBytes()));

        client.sendData();
        String s = client.receiveData();
        System.out.println(s);

        try {
            URI uri = new URI(URLGetContent.getFullUrl("47.92.166.84:8888", URLGetContent.WXInitialize));
            String host = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
