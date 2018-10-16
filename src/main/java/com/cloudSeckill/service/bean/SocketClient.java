package com.cloudSeckill.service.bean;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;

public class SocketClient {

//    private static String ipAddress = "14.20.6.190";
//    private static String ipAddress = "47.92.166.84";//阿里云
    private static String ipAddress = "47.107.87.139";//阿里云
//        private static String ipAddress = "127.0.0.1";
//    private static String ipAddress = "110.80.137.86";//快快
    private static int port = 8889;
    private OutputStream os;
    private PrintWriter pw;
    private InputStream is;
    private BufferedReader br;
    private HashMap<String, String> params = new HashMap();
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Socket socket;

    public SocketClient() {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putParam(String key, String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        params.put(key, Base64.getEncoder().encodeToString(value.getBytes()));
    }

    public void sendData() {
        try {
            if (os == null) {
                os = socket.getOutputStream();
                pw = new PrintWriter(os);//将输出流包装成打印流
            }
            //字节输出流
            pw.write(new Gson().toJson(params) + "\n");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.clear();
    }

    public String receiveData() {
        try {
            if (is == null) {
                is = socket.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
            }
            String data = "";
            String info;
            while ((info = br.readLine()) != null) {
                data += info;
            }
            System.currentTimeMillis();
            System.out.println(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
