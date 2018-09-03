package com.proxy.service;

import com.proxy.dao.ProxyUserDaoMapper;
import com.proxy.entity.RechargeCodeTypeEntity;
import com.proxy.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ProxyUserService {

    @Autowired ProxyUserDaoMapper proxyUserDaoMapper;
    @Autowired BusinessService businessService;

    /**
     * 条件查询用户列表
     */
    public List<UserEntity> queryUserListByInfo(int page, int size, String beginTime, String endTime, String userName, String remark, String status) {
        long beginTimeLong = 0;
        try {beginTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime).getTime();} catch (Exception e) {}
        long endTimeLong = 0;
        try {endTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(endTime).getTime() + 1000L * 60 * 60 * 24;} catch (Exception e) {}
        int statusInt = -1;
        try {statusInt = Integer.parseInt(status);} catch (Exception e) {}
        return proxyUserDaoMapper.queryUserListByInfo(page - 1, size, beginTimeLong, endTimeLong, userName, remark, statusInt);
    }
    
    /**
     * 查询用户数量
     */
    public int queryUserListCountByInfo(String beginTime, String endTime, String userName, String remark, String status) {
        long beginTimeLong = 0;
        try {beginTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime).getTime();} catch (Exception e) {}
        long endTimeLong = 0;
        try {endTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(endTime).getTime() + 1000L * 60 * 60 * 24;} catch (Exception e) {}
        int statusInt = -1;
        return proxyUserDaoMapper.queryUserListCountByInfo(beginTimeLong, endTimeLong, userName, remark, statusInt);
    }

    /**
     * 修改用户状态
     */
    public void alterUserStatus(String[] userIds, int status) {
        proxyUserDaoMapper.alterUserStatus(userIds,status);
    }

    /**
     * 根据用户名获取用户信息
     */
    public UserEntity getUserInfoByName(String userName) {
        return proxyUserDaoMapper.getUserInfoByUserName(userName);
    }

    /**
     * 添加用户余额
     */
    public long addUserUseTime(long rechargeCodeType, String userName){
        UserEntity userInfoByName = getUserInfoByName(userName);
        List<RechargeCodeTypeEntity> rechargeCodeTypeList = businessService.getRechargeCodeTypeList();
        long addUseTime = 0;
        for (RechargeCodeTypeEntity rechargeCodeTypeEntity : rechargeCodeTypeList){
            if(rechargeCodeTypeEntity.type == rechargeCodeType){
                addUseTime = rechargeCodeTypeEntity.use_time;
                break;
            }
        }
        proxyUserDaoMapper.addUserUseTime(userInfoByName.use_time + addUseTime, userName);
        return addUseTime;
    }
}
