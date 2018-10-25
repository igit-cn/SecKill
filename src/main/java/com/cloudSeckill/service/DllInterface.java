package com.cloudSeckill.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInterface extends Library {
    //DllInterface instance = (DllInterface) Native.loadLibrary("D:\\yql-proj-2018\\dll-test\\test", DllInterface.class);
    //DllInterface instance2 = (DllInterface) Native.loadLibrary("D:\\yql-proj-2018\\dll-test\\webapi", DllInterface.class);

    DllInterface instance = (DllInterface) Native.loadLibrary("C:\\Users\\Administrator\\Desktop\\test", DllInterface.class);
    DllInterface instance2 = (DllInterface) Native.loadLibrary("C:\\Users\\Administrator\\Desktop\\webapi", DllInterface.class);
    void webapi(String ip,String port,String UUID, String MAC, String name, String UserName, String Password, String data62);
    //DLL授权，项目第一次启动的时候调用
    int WXSetNetworkVerifyInfo(String ip, int port);

    //微信初始化，返回指针
    String WXInitialize(String name, String mac, String uuid);

    //获取二维码
    String WXGetQRCode(int object);

    //检查二维码转态
    String WXCheckQRCode(int object);

    //二维码登录
    String WXQRCodeLogin(int object, String user, String password);

    //保持心跳，二维码登陆后调用
    String WXHeartBeat(int object);

    //同步通讯录
    String WXSyncContact(int object);

    //注销、退出、下线
    String WXUserLogout(int object);

    //同步消息
    String WXSyncMessage(int object);

    //发送文字消息
    String WXSendMsg(int object, String user, String content);

    //接收红包或者转账消息
    String WXReceiveRedPacket(int object, String red_packet);

    //打开红包
    String WXOpenRedPacket(int object, String red_packet, String key);

    //接收转账
    String WXTransferOperation(int object, String transfer);

    //设置消息回调的地址
    void WXSetRecvMsgCallBack(int object, String url);

}
