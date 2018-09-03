package com.cloudSeckill.data.info;

public class UserInfo {
    public long user_id;
    public String userName;
    public String userPass;
    public String userEmail;
    public String validateCode;
    public String emailVerifyCode;

    public int status;
    public long register_time;
    public long useTime;//可以使用的最后时间

    //网络IP地址
    public String ipAddress;
    //微信登录是否成功
    public boolean isWechatLoginSuccess ;
    //服务器相关信息，如代理回复的错误描述
    public String server_msg;
    //用于跟第三方对接的token
    public String token;
    public boolean isLooperOpen;
    
}
