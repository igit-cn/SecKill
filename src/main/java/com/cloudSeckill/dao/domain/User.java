package com.cloudSeckill.dao.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class User {
    
    private int id;//坑id
    private String fromUserName;//坑所属账号名字
    private String token;//微信token
    private String wechatId;//微信唯一标示
    private String headImg;//头像
    private String name;//昵称
    private Date expirTime;//到期时间
    private int onlineStatus; //1在线 2离线
    private int createTime;//创建时间
    private int pickType;//1抢所有群 2抢指定群
    private int pickDelayTime;//延时抢的时间 秒为单位
    private int pickDelay;//是否开启延时抢  0不延时 1延时
    private String pickGroupListJson;//指定抢的群
    private int autoPickPersonal;//自动接收个人红包
    private int autoReceiveTransfer;//自动接收个人转账
    private String expirTimeStrFormat;//到期时间格式化字符串
    
    
    private int todaySum;//今日总计  临时添加返回web端使用,服务器没用
    private int income; //抢包金额  临时添加返回web端使用,服务器没用
    
    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName == null ? null : fromUserName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId == null ? null : wechatId.trim();
    }

    public Date getExpirTime() {
        return expirTime;
    }

    public void setExpirTime(Date expirTime) {
        this.expirTime = expirTime;
        this.expirTimeStrFormat = new SimpleDateFormat("yyyy年MM月dd日").format(expirTime);
    }

    public String getExpirTimeStrFormat() {
        return expirTimeStrFormat;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getPickType() {
        return pickType;
    }

    public void setPickType(int pickType) {
        this.pickType = pickType;
    }

    public int getPickDelayTime() {
        return pickDelayTime;
    }

    public void setPickDelayTime(int pickDelayTime) {
        this.pickDelayTime = pickDelayTime;
    }

    public int getPickDelay() {
        return pickDelay;
    }

    public void setPickDelay(int pickDelay) {
        this.pickDelay = pickDelay;
    }

    public List<String> getPickGroupList() {
        return new Gson().fromJson(pickGroupListJson,new TypeToken<List<String>>(){}.getType());
    }

    public void setPickGroupListJson(String pickGroupListJson) {
        this.pickGroupListJson = pickGroupListJson == null ? null : pickGroupListJson.trim();
    }

    public int getAutoPickPersonal() {
        return autoPickPersonal;
    }

    public void setAutoPickPersonal(int autoPickPersonal) {
        this.autoPickPersonal = autoPickPersonal;
    }

    public int getAutoReceiveTransfer() {
        return autoReceiveTransfer;
    }

    public void setAutoReceiveTransfer(int autoReceiveTransfer) {
        this.autoReceiveTransfer = autoReceiveTransfer;
    }

    public int getTodaySum() {
        return todaySum;
    }

    public void setTodaySum(int todaySum) {
        this.todaySum = todaySum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean addPickGroupList(String chatRoom) {
        HashSet<String> set = new Gson().fromJson(pickGroupListJson,new TypeToken<HashSet<String>>(){}.getType());
        if(set == null) {
            set = new HashSet();
        }
        int count = set.size();
        set.add(chatRoom);
        pickGroupListJson = new Gson().toJson(set);
        return count != set.size();
    }
    
    public boolean removePickGroupList(String chatRoom) {
        HashSet<String> set = new Gson().fromJson(pickGroupListJson,new TypeToken<HashSet<String>>(){}.getType());
        if(set == null) {
            set = new HashSet();
        }
        int count = set.size();
        set.remove(chatRoom);
        pickGroupListJson = new Gson().toJson(set);
        return count != set.size();
    }
}