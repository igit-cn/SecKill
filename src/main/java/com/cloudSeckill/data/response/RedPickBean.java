package com.cloudSeckill.data.response;

import com.google.gson.Gson;

import java.util.List;

public class RedPickBean {
    
    private String external;

    public External getExternal() {
        return new Gson().fromJson(external,External.class);
    }

    public class External{
        
        public List<Record> record;

        public class Record{
            
            public String userName;
            public int receiveAmount;
            
        } 
    } 
    
}
