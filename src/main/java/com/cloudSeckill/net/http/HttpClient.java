package com.cloudSeckill.net.http;

import com.cloudSeckill.net.http.callback.HttpCallBack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class HttpClient {

    public String url;

    private final RequestHttp requestHttp = new RequestHttp();

    public void setUrl(String urlName) {
        url = urlName;
        requestHttp.setUrl(urlName);
    }

    public void addParams(String key, String value) {
        requestHttp.putKeyValue(key, value);
    }

    public void send(HttpCallBack httpCallBack) {
        try {
            requestHttp.execute(httpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAsJson(HttpCallBack httpCallBack) {
        try {
            requestHttp.executeJsonPost(httpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAsSocket(HttpCallBack httpCallBack) {
        try {
            requestHttp.executeJsonSocket(httpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
