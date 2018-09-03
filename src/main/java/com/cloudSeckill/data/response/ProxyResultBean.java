package com.cloudSeckill.data.response;

public class ProxyResultBean {

    public static final int response_code_success = 0;


    public String msg;
    public int code;
    public LoginBean data;

    public boolean isResponseSuccess() {
        return this.code == response_code_success;
    }
    
    public static class LoginBean {
        
        public int id;
        public String user_name;
        public int status;
        public long register_time;
        public String remark;
        public String email;
        public long use_time;

    }
}
