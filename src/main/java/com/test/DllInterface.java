package com.test;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInterface extends Library {
    DllInterface instance1 = (DllInterface) Native.loadLibrary("D:\\works\\others\\yql-proj-2018\\CloudSeckill\\data\\clients\\dll\\test", DllInterface.class);
    DllInterface instance2 = (DllInterface) Native.loadLibrary("D:\\works\\others\\yql-proj-2018\\CloudSeckill\\data\\clients\\dll\\webapi", DllInterface.class);

    int WXSetNetworkVerifyInfo(String ip, int port);

    String WXInitialize(String name, String uuid, String mac);

    String WXGetQRCode(int object);

    String WXCheckQRCode(int object);

    int webapi(String UUID, String MAC, String name, String UserName, String Password, String data62);

    String WXQRCodeLogin(int object, String username, String password);

    String WXHeartBeat(int object);

    String WXSetRecvMsgCallBack(int object, String callbackUrl);
}
