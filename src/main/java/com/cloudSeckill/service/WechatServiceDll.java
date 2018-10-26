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
import com.cloudSeckill.utils.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proxy.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class WechatServiceDll {

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

    String name = "hahahaipad";
    String mac = "00:22:5F:5F:03:27";
    String uuid = "002IEYVG-6000-K820-28G6-M80H64RWQSUQ";
    String uuid2 = "<softtype><k3>9.0.2</k3><k9>iPad</k9><k10>2</k10><k19>58BF17B5-2D8E-4BFB-A97E-38F1226F13F8</k19><k20>" + uuid
            + "</k20><k21>neihe_5GHz</k21><k22>(null)</k22><k24>" + mac + "</k24><k33>\\345\\276\\256\\344\\277\\241</k33><k47>1</k47><k50>1</k50><k51>com.tencent.xin</k51><k54>iPad4,4</k54></softtype>";

    /**
     * 初始化微信客户端
     */
    public byte[] initWechatClient(HttpSession session, UserInfo userInfo) {
        //DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
        int object = Integer.parseInt(DllInterface.instance.WXInitialize(name, uuid2, uuid));
//        LogUtils.info("初始化uuid：" + uuid);
//        LogUtils.info("初始化mac：" + mac);
//        LogUtils.info("初始化name：" + name);
        final byte[][] content = {null};
        userInfo.token = object + "";
        content[0] = getLoginQRCode(session, userInfo);
        //重复尝试一次
        if (null == content[0]) {
            content[0] = getLoginQRCode(session, userInfo);
        }
        return content[0];
        /*DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
        int object = Integer.parseInt(DllInterface.instance.WXInitialize(name, uuid, mac));
        String qrcode = DllInterface.instance.WXGetQRCode(object);
        userInfo.token = object+"";
        looperGetWechatStatus(session, userInfo);
        GetQRCodeBean getQRCodeBean = new Gson().fromJson(qrcode, GetQRCodeBean.class);
        return Base64.getDecoder().decode(getQRCodeBean.qr_code);*/
    }

    /**
     * 获取微信登录二维码
     */
    private byte[] getLoginQRCode(HttpSession session, UserInfo userInfo) {
        final byte[][] content = {null};
        String QRString = DllInterface.instance.WXGetQRCode(Integer.parseInt(userInfo.token));
        if (!TextUtils.isEmpty(QRString)) {
            content[0] = Base64.getDecoder().decode((String) JSONObject.fromObject(QRString).get("qr_code"));
            looperGetWechatStatus(session, userInfo);//直接开轮询
        }
        return content[0];
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
                    String wxCheckQRCode = DllInterface.instance.WXCheckQRCode(Integer.parseInt(userInfo.token));
                    if (!TextUtils.isEmpty(wxCheckQRCode)) {
                        LogUtils.info(wxCheckQRCode);
                        //{"expired_time":187,"status":0}
                        QRCodeStatusBean qrCodeStatusBean = new Gson().fromJson(wxCheckQRCode, QRCodeStatusBean.class);
                        if (qrCodeStatusBean.status == 2) {//授权成功
                            userInfo.isWechatLoginSuccess = true;
                            userInfo.isLooperOpen = false;
//                                ultimatelyLogin(session, userInfo, qrCodeStatusBean);
                            macLogin(session, userInfo, qrCodeStatusBean);
                        } else if (qrCodeStatusBean.status == 3 || qrCodeStatusBean.status == 4 || qrCodeStatusBean.expired_time < 140) {//已经超时.已经取消
                            userInfo.isLooperOpen = false;
                            wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));//通知前端二维码超时
                        }
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
    public void macLogin(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        DllInterface.instance2.webapi("117.50.51.222", "1818", uuid, mac, name, qrCodeStatusBean.user_name, qrCodeStatusBean.password, "123");
        ultimatelyLogin(session, userInfo, qrCodeStatusBean);
    }

    /**
     * mac登录
     */
    public void macLoginHttp(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl("http://47.92.166.84:2223/webapi");
        httpClient.addParams("name", name);
        httpClient.addParams("mac", mac);
        httpClient.addParams("uuid", uuid);
        httpClient.addParams("user", qrCodeStatusBean.user_name);
        httpClient.addParams("password", qrCodeStatusBean.password);
        httpClient.addParams("data62", "123");
        httpClient.sendAsJson(new HttpCallBack<Object>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, Object o) {
                LogUtils.info("MAC登陆结果：" + httpClientEntity.json);
                ultimatelyLogin(session, userInfo, qrCodeStatusBean);
            }
        });

    }

    /**
     * 最终登录
     */
    private void ultimatelyLogin(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        String wxqrCodeLogin = DllInterface.instance.WXQRCodeLogin(Integer.parseInt(userInfo.token), qrCodeStatusBean.user_name, qrCodeStatusBean.password);
        LogUtils.info("ultimatelyLogin结果：" + wxqrCodeLogin);
        QRCodeLoginBean qrCodeLoginBean = new Gson().fromJson(wxqrCodeLogin, QRCodeLoginBean.class);
        if (qrCodeLoginBean.status == 0) {
            heartBeat(session, userInfo, qrCodeStatusBean);
            return;
        }
        ultimatelyLogin(session, userInfo, qrCodeStatusBean);
    }

    /**
     * 心跳
     */
    private void heartBeat(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        String wxHeartBeat = DllInterface.instance.WXHeartBeat(Integer.parseInt(userInfo.token));
        LogUtils.info("heartBeat结果：" + wxHeartBeat);
        HearBeatBean hearBeatBean = new Gson().fromJson(wxHeartBeat, HearBeatBean.class);
        if (hearBeatBean.status == 0) {
            //DllInterface.instance.WXSetRecvMsgCallBack(Integer.parseInt(userInfo.token), "http://47.106.107.116:2223/WXInitialize");
            DllInterface.instance.WXSetRecvMsgCallBack(Integer.parseInt(userInfo.token), "http://127.0.0.1/receive/notification");
            saveWechatInfo(session, userInfo, qrCodeStatusBean);
            return;
        }
    }

    /**
     * 保存微信信息
     */
    private void saveWechatInfo(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        LogUtils.info("保存WC信息");
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
        //发送初始化通知
        receiveDataController.initNotification(user.getToken(), user.getWechatId());

        //同步通讯录
        List<SyncContactBean> chainList = new ArrayList();
        syncContact(user, chainList);
        redisUtil.set(qrCodeStatusBean.user_name, chainList);


        LogUtils.info("用户 : " + user.getName() + " 扫码登录成功 来自账号 : " + user.getFromUserName());
    }

    /**
     * 同步通讯录
     */
    public void syncContact(User user, List<SyncContactBean> chainList) {
        String WeSyncContact = DllInterface.instance.WXSyncContact(Integer.parseInt(user.getToken()));
        List<SyncContactBean> syncContactBeen = new Gson().fromJson(WeSyncContact, new TypeToken<List<SyncContactBean>>() {
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

    /**
     * 退出登录
     */
    public void wechatLogout(User user) {
        String WeUserLogout = DllInterface.instance.WXUserLogout(Integer.parseInt(user.getToken()));
        HearBeatBean hearBeatBean = new Gson().fromJson(WeUserLogout, HearBeatBean.class);
        System.out.println(hearBeatBean.status);
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
