package com.cloudSeckill.service;

import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.response.QRCodeStatusBean;
import com.cloudSeckill.data.response.SyncContactBean;
import com.cloudSeckill.net.http.callback.HttpCallBack;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface WechatServiceInter {
    /**
     * 初始化微信客户端
     */
    byte[] initWechatClient(HttpSession session, UserInfo userInfo);

    /**
     * 获取微信登录二维码
     */
    byte[] getLoginQRCode(HttpSession session, UserInfo userInfo);

    /**
     * 轮询获取二维码扫描状态
     */
    void looperGetWechatStatus(HttpSession session, UserInfo userInfo);

    /**
     * 最终登录
     */
    void ultimatelyLogin(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean);

    /**
     * 心跳
     */
    void heartBeat(HttpSession session, UserInfo userInfo, QRCodeStatusBean qrCodeStatusBean);

    /**
     * 同步通讯录
     */
    void syncContact(User user, List<SyncContactBean> chainList);

    void syncMessage(User user, String token, HttpCallBack callBack);

    void sendTextMsg(User user, String token, String chatRoom, String content);

    void receiveRedPacket(User user, String token, String red_packet, String chatRoom, boolean isGroup, HttpCallBack redPickCallback);

    void pickTransfer(User user, String transfer, String token);

    /**
     * 退出登录
     */
    void wechatLogout(User user);
}
