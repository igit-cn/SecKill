package com.cloudSeckill.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInterface extends Library {
    DllInterface instance = (DllInterface) Native.loadLibrary("D:\\yql-proj-2018\\dll-test\\test", DllInterface.class);
//    DllInterface instance2 = (DllInterface) Native.loadLibrary("D:\\yql-proj-2018\\dll-test\\webapi", DllInterface.class);

    int WXSetNetworkVerifyInfo(String ip, int port);

    String WXInitialize(String name, String uuid, String mac);

    String WXGetQRCode(int object);

    String WXCheckQRCode(int object);

    int webapi(String UUID, String MAC, String name, String UserName, String Password, String data62);

    String WXQRCodeLogin(int object, String username, String password);

    String WXHeartBeat(int object);

    String WXSetRecvMsgCallBack(int object, String callbackUrl);
}
