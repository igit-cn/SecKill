package com.cloudSeckill.service.URLGetJson;

import com.cloudSeckill.utils.TextUtils;

public class URLGetContent {
    public static String url = "http://110.80.137.86:12888/api.php";
    //    public static final String ip = "http://47.92.166.84:2223/";
    //旧的接口
    private static final String ip = "http://%s:%s/";
    //socket接口
//    private static final String ip = "http://%s:8888/";
    //private static final String ip = "http://127.0.0.1:2223/";
    public static final String WXInitialize = ip + "WXInitialize";
    public static final String WXGetQRCode = ip + "WXGetQRCode";
    public static final String WXCheckQRCode = ip + "WXCheckQRCode";
    public static final String WXMacLogin = ip + "webapi";
    public static final String WXQRCodeLogin = ip + "WXQRCodeLogin";
    public static final String WXHeartBeat = ip + "WXHeartBeat";
    public static final String WXSyncMessage = ip + "WXSyncMessage";
    public static final String WXSyncContact = ip + "WXSyncContact";
    public static final String WXLogout = ip + "WXLogout";
    public static final String WXSendMsg = ip + "WXSendMsg";
    public static final String WXReceiveRedPacket = ip + "WXReceiveRedPacket";
    public static final String WXOpenRedPacket = ip + "WXOpenRedPacket";
    public static final String WXTransferOperation = ip + "WXTransferOperation";
    public static final String WXSetRecvMsgCallBack = ip + "WXSetRecvMsgCallBack";

    public static String getFullUrl(String ip, String url) {
        if (!TextUtils.equals(ip, "117.50.77.81")) {
            if (TextUtils.equals(ip, "127.0.0.1")) {
                return String.format(url, ip, "8080");
            }
            return String.format(url, ip, "2223");
        } else {
            return String.format(url, ip, "2225");
        }
    }
}
