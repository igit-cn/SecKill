package com.proxy.dao;

import com.proxy.entity.RechargeCodeEntity;

import java.util.List;

public interface RechargeCodeDaoMapper {
    
    /**
     * 查询充值码数量
     */
    long queryRechargeCodeCount(long beginTimeLong, long endTimeLong, int statusInt, long beginRechargeTimeLong, long endRechargeTimeLong,
                                String rechargeCode, String rechargeUserName, String fromProxyName, String remark, String rechargeCodeType, String sellStatus, String rechargeStatus, long sellBeginTime, long sellEndTime);
    /**
     * 查询充值码列表
     */
    List<RechargeCodeEntity> queryRechargeCodeList(int page, int size, long beginTimeLong, long endTimeLong, int statusInt, long beginRechargeTimeLong, long endRechargeTimeLong,
                                                   String rechargeCode, String rechargeUserName, String fromProxyName, String remark, String rechargeCodeType, String sellStatus, String rechargeStatus, long sellBeginTime, long sellEndTime);

    /**
     * 修改充值码状态
     */
    void resetRechargeStatus(int status, String[] rechargeIds, int proxyLevel, int proxyId, String proxyName);

    /**
     * 用户充值
     */
    void rechargeToUser(String rechargeCode, long rechargeTime, String userName);
    /**
     * 根据id获取充值码
     */
    List<RechargeCodeEntity> queryRechargeCodeById(String rechargeCodeId);
}
