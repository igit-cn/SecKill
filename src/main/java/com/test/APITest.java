package com.test;

import com.cloudSeckill.data.response.GetQRCodeBean;
import com.cloudSeckill.data.response.QRCodeStatusBean;
import com.cloudSeckill.net.http.HttpClient;
import com.cloudSeckill.net.http.callback.HttpCallBack;
import com.cloudSeckill.net.http.callback.HttpClientEntity;
import com.cloudSeckill.service.URLGetJson.URLGetContent;
import com.cloudSeckill.utils.LogUtils;
import com.opslab.util.FileUtil;
import org.springframework.web.socket.TextMessage;

import java.io.File;
import java.util.Base64;

public class APITest {
    static String ip = "127.0.0.1";

    public static void main(String[] args) {
        initWechatClient();

        //looperGetWechatStatus
        //macLogin---instance2
        //ultimatelyLogin
        //heartBeat
        //syncContact
        //wechatLogout

    }

    private static void initWechatClient() {
        String name = "hahahaipad";
        String mac = Utils.getRandomMac();
        String uuid = Utils.getRandomUUID();
        String uuid2 = "<softtype><k3>9.0.2</k3><k9>iPad</k9><k10>2</k10><k19>58BF17B5-2D8E-4BFB-A97E-38F1226F13F8</k19><k20>" + uuid
                + "</k20><k21>neihe_5GHz</k21><k22>(null)</k22><k24>" + mac + "</k24><k33>\\345\\276\\256\\344\\277\\241</k33><k47>1</k47><k50>1</k50><k51>com.tencent.xin</k51><k54>iPad4,4</k54></softtype>";
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(ip, URLGetContent.WXInitialize));
        httpClient.addParams("name", name);
        httpClient.addParams("mac", mac);
        httpClient.addParams("uuid", uuid);
        httpClient.sendAsJsonRel(new HttpCallBack<Object>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, Object str) {
                byte[] loginQRCode = getLoginQRCode(httpClientEntity.json);
                FileUtil.write(new File("D:\\tmp\\wechat\\qr.jpg"), byte2hex(loginQRCode));
                looperGetWechatStatus(httpClientEntity.json);
            }
        });
    }

    private static byte[] getLoginQRCode(String token) {
        final byte[][] content = {null};
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(ip, URLGetContent.WXGetQRCode));
        httpClient.addParams("object", token);
        httpClient.sendAsJsonRel(new HttpCallBack<GetQRCodeBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, GetQRCodeBean getQRCodeBean) {
                content[0] = Base64.getDecoder().decode(getQRCodeBean.qr_code);
            }
        });
        return content[0];
    }

    public static void looperGetWechatStatus(String token) {
        new Thread() {
            @Override
            public void run() {
                int[] expired_time = {0};
                while (true) {
                    HttpClient httpClient = new HttpClient();
                    httpClient.setUrl(URLGetContent.getFullUrl(ip, URLGetContent.WXCheckQRCode));
                    httpClient.addParams("object", token);
                    httpClient.sendAsJsonRel(new HttpCallBack<QRCodeStatusBean>() {
                        @Override
                        public void onSuccess(HttpClientEntity httpClientEntity, QRCodeStatusBean qrCodeStatusBean) {
                            LogUtils.info("轮询状态：" + qrCodeStatusBean.toString());
                            if (qrCodeStatusBean.status == 2) {//授权成功
//                                userInfo.isWechatLoginSuccess = true;
//                                userInfo.isLooperOpen = false;
//                                macLogin(session, userInfo, qrCodeStatusBean);
                            } else if (qrCodeStatusBean.status == 3 || qrCodeStatusBean.status == 4 || expired_time[0] > 140) {//已经超时.已经取消
//                                userInfo.isLooperOpen = false;
//                                wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));//通知前端二维码超时
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        expired_time[0] = expired_time[0] + 1;
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    public static String byte2hex(byte[] b) // 二进制转字符串
    {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }

        }
        return sb.toString();
    }


}
