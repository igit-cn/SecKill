package com.cloudSeckill.data.response;

public class QRCodeStatusBean {
    public String device_type;
    public int expired_time;
    public String head_url;
    public String nick_name;
    public String password;
    public int status;
    public String user_name;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public int getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(int expired_time) {
        this.expired_time = expired_time;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "QRCodeStatusBean{" +
                "device_type='" + device_type + '\'' +
                ", expired_time=" + expired_time +
                ", head_url='" + head_url + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
