package com.cloudSeckill.data.response;

import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.info.WCUserInfoCacheInfo;

public class HomeUserInfoBean {
    
    public UserInfo userInfo;
    public WCUserInfoCacheInfo wcUserInfoCacheInfo;

    public HomeUserInfoBean(UserInfo userInfo, WCUserInfoCacheInfo wcUserInfosCacheBean) {
        this.userInfo = userInfo;
        this.wcUserInfoCacheInfo = wcUserInfosCacheBean;
    }
}
