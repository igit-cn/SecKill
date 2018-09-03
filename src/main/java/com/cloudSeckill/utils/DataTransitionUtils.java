package com.cloudSeckill.utils;

import com.cloudSeckill.data.info.UserInfo;
import com.proxy.entity.UserEntity;

public class DataTransitionUtils {
    
    public static UserInfo proxyUserEntity2UserInfo(UserEntity userEntity){
        UserInfo userInfo = new UserInfo();
        if (userEntity == null) {
            userInfo.server_msg = "用户名或密码错误";
            return userInfo;
        }
        
        if(userEntity.status == 1){
            userInfo.server_msg = "用户已被冻结";
            return userInfo;
        }
        if(userEntity.status == 2){
            userInfo.server_msg = "用户已被删除";
            return userInfo;
        }

        userInfo.user_id = userEntity.id;
        userInfo.userName = userEntity.user_name;
        userInfo.userEmail = userEntity.email;
        userInfo.status = userEntity.status;
        userInfo.register_time = userEntity.register_time;
        userInfo.useTime = userEntity.use_time;
        return userInfo;
    }
}
