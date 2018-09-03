package com.cloudSeckill.dao.domain;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedPacket {
    
    public int id;
    
    public String wechat_id;
    public int user_id;
    public int money;
    public String group_id;
    public String group_name;
    public Date packet_date;
    
    //db查询使用
    public String beginPacketTime;
    public String endPacketTime;
    public List<Integer> userIdList = new ArrayList();

    public void setTodayPacketTime(Date packetTime){
        beginPacketTime = new SimpleDateFormat("yyyy-MM-dd").format(packetTime) + " 00:00:00";
        endPacketTime = new SimpleDateFormat("yyyy-MM-dd").format(packetTime) + " 23:59:59";
    }
    
}
