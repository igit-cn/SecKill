package com.cloudSeckill.net.http.callback;

public class ResultEntity {

    public static int STATUS_CODE_THREE_NOT_REGIST = 20002; //第三方登录未注册

    private int status;
    private String msg;
    private int total;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getTotal() {
        return total;
    }
}
