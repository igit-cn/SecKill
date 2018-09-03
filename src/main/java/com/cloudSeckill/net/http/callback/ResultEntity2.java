package com.cloudSeckill.net.http.callback;


public class ResultEntity2 {

    public static int STATUS_CODE_THREE_NOT_REGIST = 401; //第三方登录未注册

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
