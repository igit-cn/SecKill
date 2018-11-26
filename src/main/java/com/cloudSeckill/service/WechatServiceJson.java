package com.cloudSeckill.service;

import com.cloudSeckill.config.IpAddressConfig;
import com.cloudSeckill.controller.ReceiveDataController;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.response.*;
import com.cloudSeckill.net.http.HttpClient;
import com.cloudSeckill.net.http.callback.HttpCallBack;
import com.cloudSeckill.net.http.callback.HttpClientEntity;
import com.cloudSeckill.net.web_socket.WechatWebSocket;
import com.cloudSeckill.service.URLGetJson.URLGetContent;
import com.cloudSeckill.utils.*;
import com.proxy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//@Service
public class WechatServiceJson implements WechatServiceInter {

    @Autowired
    private IpAddressConfig ipAddressConfig;
    @Autowired
    private WechatWebSocket wechatWebSocket;
    @Autowired
    private ReceiveDataController receiveDataController;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    public static String name = "hahahaipad";
    public static String mac = Utils.getRandomMac();
    public static String uuid = Utils.getRandomUUID();


    /**
     * 初始化微信客户端
     */
    public byte[] initWechatClient(HttpSession session, UserInfo userInfo) {
        final byte[][] content = {null};
        HttpClient httpClient = new HttpClient();
        String randomIP = ipAddressConfig.getRandomIP();
//        String randomIP = redisUtil.getStr("keng_id-" + userInfo.user_id);
//        String randomIP = redisUtil.getStr("keng_id-" + SessionUtils.getCurrentSelectKengId(session));
        LogUtils.info("从缓存中获取keng-IP：" + randomIP);
        if (TextUtils.isEmpty(randomIP)) {
            randomIP = ipAddressConfig.getRandomIP();
            LogUtils.info("随机地址 : " + randomIP);
        }
        userInfo.ipAddress = randomIP;
        httpClient.setUrl(URLGetContent.getFullUrl(randomIP, URLGetContent.WXInitialize));
        httpClient.addParams("name", name);
        httpClient.addParams("mac", mac);
        httpClient.addParams("uuid", uuid);
        httpClient.sendAsJson(new HttpCallBack<Object>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, Object str) {
                userInfo.token = httpClientEntity.json;
                content[0] = getLoginQRCode(session, userInfo);
            }
        });
        return content[0];
    }

    /**
     * 获取微信登录二维码
     */
    public byte[] getLoginQRCode(HttpSession session, UserInfo userInfo) {
        final byte[][] content = {null};
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(userInfo.ipAddress, URLGetContent.WXGetQRCode));
        httpClient.addParams("object", userInfo.token);
        httpClient.sendAsJson(new HttpCallBack<GetQRCodeBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, GetQRCodeBean getQRCodeBean) {
                content[0] = Base64.getDecoder().decode(getQRCodeBean.qr_code);
            }
        });
        looperGetWechatStatus(session, userInfo);//直接开轮询
        return content[0];
    }

    /**
     * 轮询获取二维码扫描状态
     */
    public void looperGetWechatStatus(HttpSession session, UserInfo userInfo) {
        if (userInfo.isLooperOpen) {//当前用户已经有了个轮询 就不要打开了
            return;
        }
        //1. 300秒超时 轮询关闭
        //2. 我点了× 关闭
        //3. status == 2 已授权 关闭
        int[] expired_time = {0};
        userInfo.isLooperOpen = true;
        new Thread(() -> {
            while (userInfo.isLooperOpen) {
                HttpClient httpClient = new HttpClient();
                httpClient.setUrl(URLGetContent.getFullUrl(userInfo.ipAddress, URLGetContent.WXCheckQRCode));
                httpClient.addParams("object", userInfo.token);
                httpClient.sendAsJson(new HttpCallBack<QRCodeStatusBean>() {
                    @Override
                    public void onSuccess(HttpClientEntity httpClientEntity, QRCodeStatusBean qrCodeStatusBean) {
                        LogUtils.info("轮询状态：" + qrCodeStatusBean.toString());
                        if (qrCodeStatusBean.status == 2) {//授权成功
                            userInfo.isWechatLoginSuccess = true;
                            userInfo.isLooperOpen = false;
                            macLogin(session, userInfo, qrCodeStatusBean);
                        } else if (qrCodeStatusBean.status == 3 || qrCodeStatusBean.status == 4 || expired_time[0] > 140) {//已经超时.已经取消
                            userInfo.isLooperOpen = false;
                            wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));//通知前端二维码超时
                        }
                    }

                    @Override
                    public void onFailure(HttpClientEntity httpClientEntity) {
                        userInfo.isLooperOpen = false;
                        wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));//通知前端二维码超时
                    }
                });
                try {
                    Thread.sleep(1000);
                    expired_time[0] = expired_time[0] + 1;
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    /**
     * mac登录
     */
    public void macLogin(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(userInfo.ipAddress, URLGetContent.WXMacLogin));
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
    public void ultimatelyLogin(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(userInfo.ipAddress, URLGetContent.WXQRCodeLogin));
        httpClient.addParams("object", userInfo.token);
        httpClient.addParams("user", qrCodeStatusBean.user_name);
        httpClient.addParams("password", qrCodeStatusBean.password);
        httpClient.sendAsJson(new HttpCallBack<QRCodeLoginBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, QRCodeLoginBean qrCodeLoginBean) {
                if (qrCodeLoginBean.status == 0) {//登录
                    heartBeat(session, userInfo, qrCodeStatusBean);
                    return;
                }else if(qrCodeLoginBean.status == -301){
                    ultimatelyLogin(session, userInfo, qrCodeStatusBean);
                }
            }
        });
    }

    /**
     * 心跳
     */
    public void heartBeat(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(userInfo.ipAddress, URLGetContent.WXHeartBeat));
        httpClient.addParams("object", userInfo.token);
        httpClient.sendAsJson(new HttpCallBack<HearBeatBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, HearBeatBean hearBeatBean) {
                if (hearBeatBean.status == 0) {
                    saveWechatInfo(session, userInfo, qrCodeStatusBean);
                }else {
                    wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("closeQRCodeByTimeout"));
                }
            }
        });
    }

    //检查此微信之前是否曾经登陆过，并且未退出，此时把之前的微信号object退出
    private void logoutBeforeLogin(User user) {
        if (!StringUtils.isEmpty(redisUtil.getStr(user.getWechatId()))) {
            user.setToken(redisUtil.getStr(user.getWechatId()));
            wechatLogout(user);
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
        //注销此微信之前登录的痕迹
        //logoutBeforeLogin(user);
        user.setToken(userInfo.token);
        //TODO 此处保存IP到数据库
        userMapper.updateByExample(user, queryExample);
        //绑定坑id与请求服务器的地址
        redisUtil.set("keng_id-" + user.getId(), userInfo.ipAddress);
        redisUtil.set(user.getWechatId(), userInfo.token);
        //登录成功之后token绑定微信id
        receiveDataController.addToken(user);

        //发送初始化通知
        receiveDataController.sendNotification(userInfo, qrCodeStatusBean);

        //微信信息绑定,推送前端结果
        wechatWebSocket.sendMessageToUser(userInfo.userName, new TextMessage("wechatLoginSuccess"));

        //同步通讯录
        //List<SyncContactBean> chainList = new ArrayList();
        //syncContact(user, chainList);
        //redisUtil.set(qrCodeStatusBean.user_name, chainList);

        LogUtils.info("用户 : " + user.getName() + " 扫码登录成功 来自账号 : " + user.getFromUserName());
    }

    /**
     * 同步通讯录
     */
    public void syncContact(User user, List<SyncContactBean> chainList) {
        HttpClient httpClient = new HttpClient();
        String ipAddress = redisUtil.getStr("keng_id-" + user.getId() + "");
        httpClient.setUrl(URLGetContent.getFullUrl(ipAddress, URLGetContent.WXSyncContact));
        httpClient.addParams("object", user.getToken());
        httpClient.sendAsJson(new HttpCallBack<List<SyncContactBean>>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, List<SyncContactBean> syncContactBeen) {
                if (syncContactBeen.size() != 0 && syncContactBeen.get(0).isContinue != 0) {
                    for (int i = 0; i < syncContactBeen.size(); i++) {
                        if (syncContactBeen.get(i).member_count != 0) {
                            chainList.add(syncContactBeen.get(i));
                        }
                    }
                    syncContact(user, chainList);
                }
            }
        });
    }

    @Override
    public void syncMessage(User user, String token, HttpCallBack callBack) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXSyncMessage));
        httpClient.addParams("object", token);
        httpClient.sendAsJson(callBack);
    }

    @Override
    public void sendTextMsg(User user, String token, String chatRoom, String content) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXSendMsg));
        //WXSendMsg
//        httpClient.addParams("method", "V1hTZW5kTXNn");
        httpClient.addParams("object", user.getToken());
        httpClient.addParams("user", chatRoom);
        try {
            httpClient.addParams("content", Base64.getEncoder().encodeToString(content.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        httpClient.addParams("content", content);
        httpClient.sendAsJson(null);
    }

    @Override
    public void receiveRedPacket(User user, String token, String red_packet, String chatRoom, boolean isGroup, HttpCallBack redPickCallback) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXReceiveRedPacket));
        //WXReceiveRedPacket
//        httpClient.addParams("method", "V1hSZWNlaXZlUmVkUGFja2V0");
        httpClient.addParams("object", token);
        httpClient.addParams("red_packet", Base64.getEncoder().encodeToString(red_packet.getBytes()).trim().replace("\n", ""));
        //httpClient.addParams("red_packet", json);
        httpClient.sendAsJson(new HttpCallBack<ReceiveRedPacketBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, ReceiveRedPacketBean receiveRedPacketBean) {
                redPick(user, red_packet, token, receiveRedPacketBean.key, chatRoom, isGroup, redPickCallback);
            }
        });
    }

    /**
     * 抢红包
     */
    private void redPick(User user, String json, String token, String key, String chatRoom, boolean isGroup, HttpCallBack redPickCallback) {
        LogUtils.info("抢红包");
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXOpenRedPacket));
        //WXOpenRedPacket
//        httpClient.addParams("method", "V1hPcGVuUmVkUGFja2V0");
        httpClient.addParams("object", token);
        httpClient.addParams("red_packet", Base64.getEncoder().encodeToString(json.getBytes()).trim().replace("\n", ""));
        //       httpClient.addParams("red_packet", json);
        httpClient.addParams("key", key);
        httpClient.sendAsJson(redPickCallback);
    }

    @Override
    public void pickTransfer(User user, String transfer, String token) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXTransferOperation));
        //WXTransferOperation
//        httpClient.addParams("method", "V1hUcmFuc2Zlck9wZXJhdGlvbg==");
        httpClient.addParams("object", token);
        httpClient.addParams("transfer", transfer);
        httpClient.sendAsJson(null);
    }

    /**
     * 退出登录
     */
    public void wechatLogout(User user) {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(URLGetContent.getFullUrl(redisUtil.getStr("keng_id-" + user.getId()), URLGetContent.WXLogout));
        //httpClient.setUrl("http://127.0.0.1:2223/WXLogout");
        httpClient.addParams("object", user.getToken());
        httpClient.sendAsJson(new HttpCallBack<HearBeatBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, HearBeatBean hearBeatBean) {
                System.out.println(hearBeatBean.status);
            }
        });
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
        httpClient.sendAsJson(new HttpCallBack<UserStatusBean>() {
            @Override
            public void onSuccess(HttpClientEntity httpClientEntity, UserStatusBean userStatusBean) {
                userName[0] = userStatusBean.user_name;
            }
        });
        return StringUtils.isEmpty(userName[0]) ? 2 : 1;
    }
}
