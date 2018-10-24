package com.cloudSeckill.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInterfaceWebApi extends Library {
    DllInterfaceWebApi instance = (DllInterfaceWebApi) Native.loadLibrary("D:\\yql-proj-2018\\dll-test\\webapi", DllInterfaceWebApi.class);

    int webapi(String UUID, String MAC, String name, String UserName, String Password, String data62);
}
