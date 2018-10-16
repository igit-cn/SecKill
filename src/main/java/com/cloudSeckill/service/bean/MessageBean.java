package com.cloudSeckill.service.bean;

import com.google.gson.annotations.SerializedName;

public class MessageBean {
    private String content;
    @SerializedName("continue")
    private int mContinue;
    private String description;
    private String from_user;
    private String msg_id;
    private String msg_source;
    private int msg_type;
    private int status;
    private int sub_type;
    private int timestamp;
    private long uin;
    private String to_user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getmContinue() {
        return mContinue;
    }

    public void setmContinue(int mContinue) {
        this.mContinue = mContinue;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public int getSub_type() {
        return sub_type;
    }

    public void setSub_type(int sub_type) {
        this.sub_type = sub_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_source() {
        return msg_source;
    }

    public void setMsg_source(String msg_source) {
        this.msg_source = msg_source;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public long getUin() {
        return uin;
    }

    public void setUin(long uin) {
        this.uin = uin;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content:\"" + content + "\"" + "," +
                ", continue:" + mContinue + "," +
                ", description：\"" + description + "\"" + "," +
                ", from_user：\"" + from_user + "\"" + "," +
                ", msg_id：\"" + msg_id + "\"" + "," +
                ", msg_source：\"" + msg_source + "\"" + "," +
                ", msg_type:" + msg_type + "," +
                ", status:" + status + "," +
                ", sub_type:" + sub_type + "," +
                ", timestamp:" + timestamp + "," +
                ", uin:" + uin + "," +
                ", to_user:\"" + to_user + "\"" +
                "}";
    }
}

