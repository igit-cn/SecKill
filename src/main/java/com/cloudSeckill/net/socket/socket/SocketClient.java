package com.cloudSeckill.net.socket.socket;

import com.google.gson.Gson;
import com.proxy.utils.StringUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Base64;
import java.util.HashMap;

public class SocketClient {

    private static String ipAddress;
    private static int port;
    private OutputStream os;
    private PrintWriter pw;
    private InputStream is;
    private BufferedReader br;

    public static void init(String ipAddress, int port) {
        SocketClient.ipAddress = ipAddress;
        SocketClient.port = port;
    }

    private HashMap<String, String> params = new HashMap();

    private Socket socket;

    public SocketClient() {
        try {
            socket = new Socket(ipAddress, port);
        } catch (Exception e) {
//            e.printStackTrace();
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
            pw.write(new Gson().toJson(params));
            pw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
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
            return data;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "";
    }
}
