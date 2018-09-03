package com.proxy.entity;


public class RechargeCodeSellOrUsedParamEntity {
    
    /** --------- 转类型 ---------- */
    public String buyBeginTime; //create_time
    public String buyEndTime;
    public String sellBeginTime;//sell_time
    public String sellEndTime;
    public String rechargeBeginTime; //recharge_time
    public String rechargeEndTime;
    /** --------- 转类型 ---------- */
    public long buyBeginTimeLong; 
    public long buyEndTimeLong;
    public long sellBeginTimeLong;
    public long sellEndTimeLong;
    public long rechargeBeginTimeLong;
    public long rechargeEndTimeLong;
    /** --------- 转类型 ---------- */

    public String token;
    public int page;
    public int size;
    public String remark;
    public String proxyName;
    public String rechargeCode;
    public String rechargeType;
    public String status; //0 1 2 
    public String rechargeStatus; // 0是未充值 1是已充值
    public String sellStatus; //0是未出售 1是已出售
    
    public String childProxyName;
    public String fromType; //0是自己 1是下级代理
}
