package com.cloudSeckill.service;

import com.cloudSeckill.config.IpAddressConfig;
import com.cloudSeckill.controller.ReceiveDataControllerDll;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.response.*;
import com.cloudSeckill.net.http.HttpClient;
import com.cloudSeckill.net.http.callback.HttpCallBack;
import com.cloudSeckill.net.http.callback.HttpClientEntity;
import com.cloudSeckill.net.web_socket.WechatWebSocket;
import com.cloudSeckill.service.URLGetJson.*;
import com.cloudSeckill.service.bean.QRBean;
import com.cloudSeckill.utils.RedisUtil;
import com.cloudSeckill.utils.SessionUtils;
import com.cloudSeckill.utils.TextUtils;
import com.cloudSeckill.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.proxy.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.MediaType;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class WechatApiService {

    @Autowired
    private IpAddressConfig ipAddressConfig;
    @Autowired
    private WechatWebSocket wechatWebSocket;
    @Autowired
    private ReceiveDataControllerDll receiveDataController;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;


    //    private QRStatusBean qRStatusBean;
    private String redJson;
    private String key;
    private WXInitializeBean wxInitializeBean;

    /**
     * 初始化微信客户端
     */
    public byte[] initWechatClient(HttpSession session, UserInfo userInfo) {
        wxInitializeBean = new WXInitializeBean();
        wxInitializeBean.setName("lllllipad");
        wxInitializeBean.setMac(Utils.getRandomMac());
        wxInitializeBean.setUuid(Utils.getRandomUUID());
        String json = Base64.getEncoder().encodeToString(new Gson().toJson(wxInitializeBean).getBytes()).trim().replace("\n", "");
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXInitialize)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();
            if (null != response && response.isSuccessful()) {
                String string = response.body().string();
                System.out.println(string);
                if (!TextUtils.isEmpty(string)) {
                    userInfo.token = string;
                    return getLoginQRCode(session, userInfo);
                }
            } else {
                System.out.println("网络请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取微信登录二维码
     */
    private byte[] getLoginQRCode(HttpSession session, UserInfo userInfo) {
        WXGetQRCodeBean wxGetQRCodeBean = new WXGetQRCodeBean();
        wxGetQRCodeBean.setObject(userInfo.token);
        String json = Base64.getEncoder().encodeToString(new Gson().toJson(wxGetQRCodeBean).getBytes()).trim().replace("\n", "");
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXGetQRCode)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();
            if (null != response && response.isSuccessful()) {
                String str = response.body().string();
                final QRBean qrBean = new Gson().fromJson(str, QRBean.class);
                byte[] data = Base64.getDecoder().decode(qrBean.getQr_code());
                //进入轮询
                looperGetWechatStatus(session, userInfo);
                return data;
            } else {
                System.out.println("网络请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 轮询获取二维码扫描状态
     */
    private void looperGetWechatStatus(HttpSession session, UserInfo userInfo) {
        if (userInfo.isLooperOpen) {//当前用户已经有了个轮询 就不要打开了
            return;
        }
        //1. 300秒超时 轮询关闭
        //2. 我点了× 关闭
        //3. status == 2 已授权 关闭
        new Thread() {
            @Override
            public void run() {
                userInfo.isLooperOpen = true;
                while (userInfo.isLooperOpen) {
                    WXGetQRCodeBean wxGetQRCodeBean = new WXGetQRCodeBean();
                    wxGetQRCodeBean.setObject(userInfo.token);
                    String json = Base64.getEncoder().encodeToString(new Gson().toJson(wxGetQRCodeBean).getBytes()).trim().replace("\n", "");
                    try {
                        Response response = OkHttpUtils.postString()
                                .url(URLGetContent.WXCheckQRCode)
                                .content(json)
                                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                                .build()
                                .execute();
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            QRCodeStatusBean qrCodeStatusBean = new Gson().fromJson(str, QRCodeStatusBean.class);
                            System.out.println("status:" + qrCodeStatusBean.status);
                            if (qrCodeStatusBean.status == 2) {
                                //成功
                                userInfo.isWechatLoginSuccess = true;
                                userInfo.isLooperOpen = false;
//                                ultimatelyLogin(session, userInfo, qrCodeStatusBean);
                                macLogin(session, userInfo, str);
                            } else if (qrCodeStatusBean.status == 3 || qrCodeStatusBean.status == 4 || qrCodeStatusBean.expired_time < 140) {//已经超时.已经取消
                                userInfo.isLooperOpen = false;
                                wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));//通知前端二维码超时
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    /**
     * mac登录
     */
    public void macLogin(HttpSession session, UserInfo userInfo, String qrStatusInfo) {
        QRCodeStatusBean qRStatusBean = new Gson().fromJson(qrStatusInfo, QRCodeStatusBean.class);
        WXMacLoginBean wxMacLoginBean = new WXMacLoginBean();
        wxMacLoginBean.setName(wxInitializeBean.getName());
        wxMacLoginBean.setMac(wxInitializeBean.getMac());
        wxMacLoginBean.setUuid(wxInitializeBean.getUuid());
        wxMacLoginBean.setUser(qRStatusBean.user_name);
        wxMacLoginBean.setPassword(qRStatusBean.password);
        wxMacLoginBean.setData62("123");
        String json = new Gson().toJson(wxMacLoginBean);
        json = Base64.getEncoder().encodeToString(json.getBytes()).trim().replace("\n", "");
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXMacLogin)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                //{"status":= 0}
                System.out.println(result);
                ultimatelyLogin(session, userInfo, qrStatusInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最终登录
     */
    private void ultimatelyLogin(HttpSession session, UserInfo userInfo, String qrCodeStatusInfo) {
        final int[] count = {0};
        QRCodeStatusBean qrCodeStatusBean = new Gson().fromJson(qrCodeStatusInfo, QRCodeStatusBean.class);
        WXQRCodeLoginBean wxqrCodeLoginBean = new WXQRCodeLoginBean();
        wxqrCodeLoginBean.setObject(userInfo.token);
        wxqrCodeLoginBean.setUser(qrCodeStatusBean.getUser_name());
        wxqrCodeLoginBean.setPassword(qrCodeStatusBean.getPassword());
        String json = Base64.getEncoder().encodeToString(new Gson().toJson(wxqrCodeLoginBean).getBytes()).trim().replace("\n", "");
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXQRCodeLogin)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();
            if (response.isSuccessful()) {
                QRCodeLoginBean qrCodeLoginBean = new Gson().fromJson(response.body().string(), QRCodeLoginBean.class);
                count[0]++;
                if (count[0] == 3) {
                    //失败三次 WebSocket异步通知
                    return;
                }
                if (qrCodeLoginBean.status == 0) {
                    heartBeat(session, userInfo, qrCodeStatusBean);
                    return;
                }
                ultimatelyLogin(session, userInfo, qrCodeStatusInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 心跳
     */
    private void heartBeat(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        final int[] count = {0};
        WXGetQRCodeBean wxGetQRCodeBean = new WXGetQRCodeBean();
        wxGetQRCodeBean.setObject(userInfo.token);
        String json = Base64.getEncoder().encodeToString(new Gson().toJson(wxGetQRCodeBean).getBytes()).trim().replace("\n", "");
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXHeartBeat)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8")).build()
                    .execute();
            if (response.isSuccessful()) {
                HearBeatBean hearBeatBean = new Gson().fromJson(response.body().string(), HearBeatBean.class);
                count[0]++;
                if (count[0] == 3) {
                    //TODO 失败三次 WebSocket异步通知
                    return;
                }
                if (hearBeatBean.status != 0) {
                    heartBeat(session, userInfo, qrCodeStatusBean);
                    return;
                }
                saveWechatInfo(session, userInfo, qrCodeStatusBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存微信信息
     */
    private void saveWechatInfo(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        //先清理掉同名的微信id绑定关系
        User updateUser = new User();
        updateUser.setWechatId(qrCodeStatusBean.user_name);
        userMapper.updateByWechatId(updateUser);

        //保存数据到坑中
        int id = SessionUtils.getCurrentSelectKengId(session);
        //数据保存Bean中
        UserExample queryExample = new UserExample();
        queryExample.createCriteria().andIdEqualTo(id);
        List<User> queryUserList = userMapper.selectByExample(queryExample);
        User user = queryUserList.get(0);
        user.setWechatId(qrCodeStatusBean.user_name);
        user.setUserId(qrCodeStatusBean.user_name);
        user.setHeadImg(qrCodeStatusBean.head_url);
        user.setName(qrCodeStatusBean.nick_name);
        user.setOnlineStatus(1);
        user.setToken(userInfo.token);
        //TODO 此处保存IP到数据库
        userMapper.updateByExample(user, queryExample);
        //绑定坑id与请求服务器的地址
        redisUtil.set("keng_id-" + user.getId(), userInfo.ipAddress);
        //微信信息绑定,推送前端结果
        wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("wechatLoginSuccess"));
        //登录成功之后token绑定微信id
        receiveDataController.addToken(user);


        //同步通讯录
        List<SyncContactBean> chainList = new ArrayList();
        syncContact(user, chainList);
        redisUtil.set(qrCodeStatusBean.user_name, chainList);

        //发送初始化通知
        receiveDataController.initNotification(user.getToken(), user.getWechatId());

        System.out.println("用户 : " + user.getName() + " 扫码登录成功 来自账号 : " + user.getFromUserName());
    }

    /**
     * 同步通讯录
     */
    public void syncContact(User user, List<SyncContactBean> chainList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("object", user.getToken());
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXSyncContact)
                    .content(Base64.getEncoder().encodeToString(jsonObject.toString().getBytes()).trim().replace("\n", ""))
                    .mediaType(MediaType.parse("application/json; charset=utf-8")).build()
                    .execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                System.out.println("WXSyncContact:" + result);
                List<SyncContactBean> syncContactBeen = new Gson().fromJson(result, new TypeToken<List<SyncContactBean>>() {
                }.getType());
                if (syncContactBeen.size() != 0 && syncContactBeen.get(0).isContinue != 0) {
                    for (int i = 0; i < syncContactBeen.size(); i++) {
                        if (syncContactBeen.get(i).member_count != 0) {
                            chainList.add(syncContactBeen.get(i));
                        }
                    }
                    syncContact(user, chainList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HttpClient httpClient = new HttpClient();
//        String ipAddress = redisUtil.getStr("keng_id-" + user.getId() + "");
//        httpClient.setUrl("http://" + ipAddress + ":12888/api.php");
//        httpClient.addParams("method", "WXSyncContact");
//        httpClient.addParams("object", user.getToken());
//        httpClient.send(new HttpCallBack<List<SyncContactBean>>() {
//            @Override
//            public void onSuccess(HttpClientEntity httpClientEntity, List<SyncContactBean> syncContactBeen) {
//                if (syncContactBeen.size() != 0 && syncContactBeen.get(0).isContinue != 0) {
//                    for (int i = 0; i < syncContactBeen.size(); i++) {
//                        if (syncContactBeen.get(i).member_count != 0) {
//                            chainList.add(syncContactBeen.get(i));
//                        }
//                    }
//                    syncContact(user, chainList);
//                }
//            }
//        });
    }

    /**
     * 退出登录
     */
    public void wechatLogout(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("object", user.getToken());
        try {
            Response response = OkHttpUtils.postString()
                    .url(URLGetContent.WXExtDeviceLogout)
                    .content(jsonObject.toString())
                    .mediaType(MediaType.parse("application/json; charset=utf-8")).build()
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        HttpClient httpClient = new HttpClient();
//        httpClient.setUrl("http://" + redisUtil.getStr("keng_id-" + user.getId()) + ":12888/api.php");
//        httpClient.addParams("method", "WXExtDeviceLogout");
//        httpClient.addParams("object", user.getToken());
//        httpClient.send(new HttpCallBack<HearBeatBean>() {
//            @Override
//            public void onSuccess(HttpClientEntity httpClientEntity, HearBeatBean hearBeatBean) {
//                System.out.println(hearBeatBean.status);
//            }
//        });
    }

    /**
     * 获取在线离线状态
     */
    public int getUserStatusIsLogin(User user) {
        if (StringUtils.isEmpty(user.getWechatId())) {
            return 2;
        }
        final String[] userName = new String[1];
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl("http://" + redisUtil.getStr("keng_id-" + user.getId()) + ":12888/api.php");
        httpClient.addParams("method", "WXGetContact");
        httpClient.addParams("object", user.getToken());
        httpClient.addParams("user", user.getWechatId());
        httpClient.send(new HttpCallBack<UserStatusBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, UserStatusBean userStatusBean) {
                userName[0] = userStatusBean.user_name;
            }
        });
        return StringUtils.isEmpty(userName[0]) ? 2 : 1;
    }
}
