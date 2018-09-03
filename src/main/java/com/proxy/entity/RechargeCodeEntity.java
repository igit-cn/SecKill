package com.proxy.entity;

public class RechargeCodeEntity {
    
    public int id;
    
    public String recharge_code;
    public int status;
    public int status_edit_level;
    public String status_edit_name;
    public String from_proxy_name;
    public int from_proxy_id;
    public long create_time;
    public int recharge_type;
    public String recharge_type_tips;
    public String remark;
    public String tips;
    
    public long make_over_time = 0;
    
    public int sell_status;
    public long sell_time;
    public String sell_remark;
    
    public int recharge_user_id;
    public String recharge_account;
    public int recharge_status;
    public long recharge_time;
    
    public int showStatus;
}
