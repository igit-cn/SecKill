package com.cloudSeckill.data.response;

import com.google.gson.annotations.SerializedName;

public class SyncContactBean {
    
    //群成员数量
    public int member_count;
    
    //昵称/群名
    public String nick_name;
    
    //群ID
    public String user_name;

    //哦判断是否有下一条
    @SerializedName("continue") 
    public int isContinue;
}
