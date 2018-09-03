package com.proxy.constant;

public class ResponseCode {

    public static final int DATA_ERROR = -1; //错误码: 数据传空或参数格式
    public static final int LOGIN_ERROR = -2; //账号或者密码错误
    public static final int PROXY_ACCOUNT_NOT_EXIST = -3;//代理不存在
    public static final int CURRENT_PROXY_ACCOUNT_EXIST = -4;//当前代理账号已经存在,无法注册
    public static final int CURRENT_PROXY_EMAIL_EXIST = -5;//当前代理账号的邮箱已经存在,无法注册
    public static final int EMAIL_NOT_EXIST = -6; // 邮箱不存在
    public static final int EMAIL_SEND_ERROR = -7; //邮箱发送失败
    public static final int RANDOM_CODE_TIMEOUT = -8;//验证码不存在或者已超时
    public static final int TOKEN_TIMEOUT = -9 ; //token失效
    public static final int PERMISSION_REFUSE = -10; //权限拒绝
    public static final int RECHARGE_CODE_INSUFFICIENT = -11;//充值码剩余数量不足
    public static final int RECHARGE_CODE_TYPE_EXCEPTION = -12; //充值码类型不正确
    public static final int RECHARGE_CODE_NOT_EXIST = -13; //充值码不存在
    public static final int USER_NOT_EXIST = -14;//用户不存在
    public static final int RECHARGE_CODE_NOT_SELL = -15;//充值码未出售,无法充值
    public static final int RECHARGE_CODE_RECHARGEED = -16;//充值码已被充值,无法使用
    public static final int RECHARGE_CODE_STATUS_ERR = -17;//充值码状态异常
    
    public static final int RESPONSE_OK = 0; //操作成功

}
