package com.proxy.service;

import com.proxy.dao.RechargeCodeDaoMapper;
import com.proxy.entity.RechargeCodeEntity;
import com.proxy.entity.RechargeCodeTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RechargeCodeService {

    @Autowired
    RechargeCodeDaoMapper rechargeCodeDaoMapper;
    @Autowired
    BusinessService businessService;

    /**
     * 条件选择查询充值码数量
     */
    public long queryRechargeCodeCount(String beginTime, String endTime, String status, String rechargeBeginTime,
           String rechargeEndTime, String rechargeCode, String rechargeCodeType, String rechargeUserName, String fromProxyName, String remark,String sellStatus,String rechargeStatus) {
        long beginTimeLong = 0;
        try {
            beginTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime).getTime();
        } catch (Exception e) {}
        long endTimeLong = 0;
        try {
            endTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(endTime).getTime() + 1000L * 60 * 60 * 24;
        } catch (Exception e) {}
        int statusInt = -1;
        try {
            statusInt = Integer.parseInt(status);
        } catch (Exception e) {}
        long beginRechargeTimeLong = 0;
        try {
            beginRechargeTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(rechargeBeginTime).getTime();
        } catch (Exception e) {}
        long endRechargeTimeLong = 0;
        try {
            endRechargeTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(rechargeEndTime).getTime() + 1000L * 60 * 60 * 24;
        } catch (Exception e) {}
        return rechargeCodeDaoMapper.queryRechargeCodeCount(beginTimeLong, endTimeLong, statusInt, beginRechargeTimeLong,
                endRechargeTimeLong, rechargeCode, rechargeUserName, fromProxyName, remark,rechargeCodeType,sellStatus,rechargeStatus,0,0);
    }
    
    /**
     * 条件选择查询充值码列表
     */
    public List<RechargeCodeEntity> queryRechargeCodeList(int page, int size, String beginTime, String endTime, String status, String rechargeBeginTime, 
          String rechargeEndTime, String rechargeCode,String rechargeCodeType, String rechargeUserName, String fromProxyName, String remark,String sellStatus,String rechargeStatus) {

        long beginTimeLong = 0;
        try {
            beginTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime).getTime();
        } catch (Exception e) {}
        long endTimeLong = 0;
        try {
            endTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(endTime).getTime() + 1000L * 60 * 60 * 24;
        } catch (Exception e) {}
        int statusInt = -1;
        try {
            statusInt = Integer.parseInt(status);
            if(statusInt == 1){//已充值
                statusInt = -1;
                rechargeStatus = "1";
            } else if(statusInt == 2){//已出售
                statusInt = -1;
                sellStatus = "1";
            } else if(statusInt == 3){ //已冻结
                statusInt = 1;
            } else if(statusInt == 4){//已删除
                statusInt = 2;
            }
            
        } catch (Exception e) {}
        long beginRechargeTimeLong = 0;
        try {
            beginRechargeTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(rechargeBeginTime).getTime();
        } catch (Exception e) {}
        long endRechargeTimeLong = 0;
        try {
            endRechargeTimeLong = new SimpleDateFormat("yyyy-MM-dd").parse(rechargeEndTime).getTime() + 1000L * 60 * 60 * 24;
        } catch (Exception e) {}

        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeList(page - 1, size, beginTimeLong, endTimeLong, statusInt, beginRechargeTimeLong,
                endRechargeTimeLong, rechargeCode, rechargeUserName, fromProxyName, remark, rechargeCodeType, sellStatus, rechargeStatus, 0, 0);
        
        //初始化显示状态
        for (RechargeCodeEntity rechargeCodeEntity : rechargeCodeEntities) {
            if(rechargeCodeEntity.status == 1){
                rechargeCodeEntity.showStatus = 1;//已冻结
                continue;
            }
            
            if(rechargeCodeEntity.status == 2){
                rechargeCodeEntity.showStatus = 2;//已删除
                continue;
            }
            
            if (rechargeCodeEntity.recharge_status == 1) {
                rechargeCodeEntity.showStatus = 4;//已充值
                continue;
            }
            
            if (rechargeCodeEntity.sell_status == 1) {
                rechargeCodeEntity.showStatus = 3;//已出售
                continue;
            }
            
            rechargeCodeEntity.showStatus = 0;//正常未出售
        }
        //初始化卡类型
        for (RechargeCodeEntity rechargeCodeEntity : rechargeCodeEntities){
            List<RechargeCodeTypeEntity> rechargeCodeTypeList = businessService.getRechargeCodeTypeList();
            rechargeCodeEntity.recharge_type_tips = "未知类型";
            for (RechargeCodeTypeEntity rechargeCodeTypeEntity: rechargeCodeTypeList){
                if(rechargeCodeEntity.recharge_type == rechargeCodeTypeEntity.type){
                    rechargeCodeEntity.recharge_type_tips = rechargeCodeTypeEntity.tips;
                }
            }
        }
        return rechargeCodeEntities; 
    }
    /**
     * 获取充值码根据id
     */
    public RechargeCodeEntity queryRechargeCodeById(String rechargeCodeId){
        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeById(rechargeCodeId);
        if(rechargeCodeEntities.size() >= 1){
            return rechargeCodeEntities.get(0);
        }
        return null;
    }

    /**
     * 用户充值,改变充值吗状态
     */
    public void rechargeToUser(String rechargeCode,String userName){
        rechargeCodeDaoMapper.rechargeToUser(rechargeCode,new Date().getTime(),userName);
    }
    
    /**
     * 修改充值码状态
     */
    public void resetRechargeStatus(int status, String[] rechargeIds,int proxyLevel,int proxyId,String proxyName) {
        rechargeCodeDaoMapper.resetRechargeStatus(status,rechargeIds,proxyLevel,proxyId,proxyName);
    }
}
