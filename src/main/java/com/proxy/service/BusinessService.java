package com.proxy.service;

import com.proxy.dao.BusinessDaoMapper;
import com.proxy.dao.FeedbackDaoMapper;
import com.proxy.dao.ProxyUserDaoMapper;
import com.proxy.dao.RechargeCodeDaoMapper;
import com.proxy.entity.*;
import com.proxy.utils.NumberFormatUtils;
import com.proxy.utils.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private ProxyService proxyService;
    @Autowired
    private BusinessDaoMapper businessDaoMapper;
    @Autowired
    private FeedbackDaoMapper feedbackDaoMapper;
    @Autowired
    private RechargeCodeDaoMapper rechargeCodeDaoMapper;
    @Autowired
    private ProxyUserDaoMapper proxyUserDaoMapper;

    public void pushRechargeCard(String fromProxyName, String firstStr,int rechargeType, int count, String remark) {
        ProxyInfoEntity proxyInfoByAccount = proxyService.getProxyInfoByAccount(fromProxyName);

        for (int i = 0; fromProxyName != null && i < count; i++) {//批量插入生成的充值码
            String rechargeCode = RandomStringUtils.randomString(firstStr,10);
            long createTime = new Date().getTime();
            RechargeCodeEntity rechargeCodeEntity = new RechargeCodeEntity();
            rechargeCodeEntity.create_time = createTime;
            rechargeCodeEntity.from_proxy_id = proxyInfoByAccount.id;
            rechargeCodeEntity.from_proxy_name = proxyInfoByAccount.proxy_name;
            rechargeCodeEntity.recharge_type = rechargeType;
            rechargeCodeEntity.remark = remark;
            rechargeCodeEntity.recharge_code = rechargeCode;

            businessDaoMapper.pushRechargeCard(rechargeCodeEntity);
            businessDaoMapper.makeOverRechargeCode(rechargeCode, "root", fromProxyName, remark, createTime, rechargeType);
        }
        
    }

    public void addRemark(int id, String proxyName, String remark) {
        businessDaoMapper.addRemark(id, proxyName, remark);
    }
    
    public long queryProxyListCountByPreviousProxyName(String previous_proxy_name) {
        return businessDaoMapper.queryProxyListCountByPreviousProxyName(previous_proxy_name);
    }

    public List<ProxyListEntity> queryProxyListByPreviousProxyName(String previous_proxy_name, int page, int size) {
        return businessDaoMapper.queryProxyListByPreviousProxyName(previous_proxy_name, page - 1, size);
    }


    public void makeOverRechargeCode(String proxyName, String[] rechargeCodes, String toProxyName, String remark) {
        long time = new Date().getTime();
        for (String rechargeCode : rechargeCodes) {
            RechargeCodeTransferEntity rechargeCodeTransferEntity = businessDaoMapper.queryRechargeByCodeWithToProxyName(proxyName, rechargeCode);
            if (rechargeCodeTransferEntity != null) {
                ProxyInfoEntity proxyInfoByAccount = proxyService.getProxyInfoByAccount(toProxyName);
                if (proxyInfoByAccount == null) {
                    return;
                }

                businessDaoMapper.makeOverRechargeCode(rechargeCode, proxyName, toProxyName, remark, time, rechargeCodeTransferEntity.recharge_code_type);
                businessDaoMapper.makeOverRechargeCodeTableRechargeCode(rechargeCode, proxyInfoByAccount.proxy_name, proxyInfoByAccount.id, time);
            }
        }
    }

    public int makeOverRechargeCodeByType(String proxyName, String rechargeCodeType, String count, String toProxyName, String remark) {
        List<RechargeCodeTypeEntity> rechargeCodeTypeList = getRechargeCodeTypeList();
         boolean typeCheck = false;
        for (RechargeCodeTypeEntity item : rechargeCodeTypeList){
            if(item.type == NumberFormatUtils.integerFormat(rechargeCodeType)){
                typeCheck = true;
                break;
            }
        }
        if( ! typeCheck){
            return 1;//返回值1代表没有找到正确的type类型
        }
        
        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeList
                (0, NumberFormatUtils.integerFormat(count), 0, 0, -1, 0, 0, null, null, proxyName, null, rechargeCodeType, "0", null,0, 0);
        if(rechargeCodeEntities.size() < NumberFormatUtils.integerFormat(count)){
            return 2;//超过了剩余数量
        }
        String rechargeCode[] = new String[rechargeCodeEntities.size()];
        for (RechargeCodeEntity item : rechargeCodeEntities){
            rechargeCode[rechargeCodeEntities.indexOf(item)] = item.recharge_code;
        }
        
        makeOverRechargeCode(proxyName,rechargeCode,toProxyName,remark);
        return 3;//成功
    }

    public List<RechargeCodeTypeEntity> getRechargeCodeTypeList() {
        return businessDaoMapper.getRechargeCodeTypeList();
    }

    public List<MakeOverCountEntity> getMakeOverChildDetail(String proxyName, String childProxyName) {
        List<MakeOverCountEntity> makeOverCountEntities = new ArrayList();
        
        List<RechargeCodeTransferEntity> makeOverToChild = businessDaoMapper.getMakeOverToChild(proxyName, childProxyName);
        int count = 0;
        long time = 0;
        int rechargeCodeType = 0;
        for (int i = 0; i < makeOverToChild.size(); i++) {
            if(rechargeCodeType == 0){//首次赋初始值
                count = 1;
                rechargeCodeType = makeOverToChild.get(i).recharge_code_type;
                time = makeOverToChild.get(i).transfer_time;
            } else if(rechargeCodeType == makeOverToChild.get(i).recharge_code_type){ //发现相同累加
                count += 1;
            } else { //不同,即保存,然后清空
                MakeOverCountEntity makeOverCountEntity = new MakeOverCountEntity();
                makeOverCountEntity.count = count;
                makeOverCountEntity.time = time;
                makeOverCountEntity.type = rechargeCodeType;
                makeOverCountEntities.add(makeOverCountEntity);
                rechargeCodeType = 0;
            }
        }
        
        if(makeOverToChild.size() != 0){
            MakeOverCountEntity makeOverCountEntity = new MakeOverCountEntity();
            makeOverCountEntity.count = count;
            makeOverCountEntity.time = time;
            makeOverCountEntity.type = rechargeCodeType;
            makeOverCountEntities.add(makeOverCountEntity);
        }

        return makeOverCountEntities;
    }


    public List<MakeOverCountEntity> getSurplusRechargeCountList(String proxyName) {
        List<RechargeCodeTypeEntity> rechargeCodeTypeList = getRechargeCodeTypeList();
        List<MakeOverCountEntity> makeOverCountEntities = new ArrayList();

        for (RechargeCodeTypeEntity rechargeCodeTypeEntity : rechargeCodeTypeList) {
            int surplusRechargeCount = businessDaoMapper.getSurplusRechargeCount(rechargeCodeTypeEntity.type, proxyName);
            if (surplusRechargeCount != 0) {
                MakeOverCountEntity makeOverCountEntity = new MakeOverCountEntity();
                makeOverCountEntity.type = rechargeCodeTypeEntity.type;
                makeOverCountEntity.count = surplusRechargeCount;
                makeOverCountEntity.tips = rechargeCodeTypeEntity.tips;
                makeOverCountEntities.add(makeOverCountEntity);
            }
        }
        return makeOverCountEntities;
    }

    public List<RechargeCodeEntity> getSurplusRechargeList(String proxyName, int type, int page, int size) {
        return businessDaoMapper.getSurplusRechargeList(proxyName, type, page - 1, size);
    }
    
    public long getSurplusRechargeListCount(String proxyName, int type){
        return businessDaoMapper.getSurplusRechargeListCount(proxyName, type);
    }

    public void addFeedback(String proxyName, String content) {
        feedbackDaoMapper.addFeedback(proxyName, content, new Date().getTime());
    }

    public ListEntity getRechargeCodeTransferOrUsed(RechargeCodeSellOrUsedParamEntity paramEntity, ProxyInfoEntity proxyInfoEntity) {
        paramEntity.buyBeginTimeLong = NumberFormatUtils.dateFormat(paramEntity.buyBeginTime);
        paramEntity.buyEndTimeLong = NumberFormatUtils.dateFormatTomorrow(paramEntity.buyEndTime);
        paramEntity.sellBeginTimeLong = NumberFormatUtils.dateFormat(paramEntity.sellBeginTime);
        paramEntity.sellEndTimeLong = NumberFormatUtils.dateFormatTomorrow(paramEntity.sellEndTime);
        paramEntity.rechargeBeginTimeLong = NumberFormatUtils.dateFormat(paramEntity.rechargeBeginTime);
        paramEntity.rechargeEndTimeLong = NumberFormatUtils.dateFormatTomorrow(paramEntity.rechargeEndTime);
        paramEntity.proxyName = proxyInfoEntity.proxy_name;
        paramEntity.page = paramEntity.page - 1;

        //先查询出售出去的列表
        if ("0".equals(paramEntity.fromType)) {
            List<RechargeCodeEntity> resultList = businessDaoMapper.getRechargeCodeUsed(paramEntity);
            //类型tips补全
            List<RechargeCodeTypeEntity> rechargeCodeTypeList = getRechargeCodeTypeList();
            for (RechargeCodeEntity rechargeCodeEntity : resultList){
                for (RechargeCodeTypeEntity rechargeCodeTypeEntity : rechargeCodeTypeList){
                    if(rechargeCodeEntity.recharge_type == rechargeCodeTypeEntity.type){
                        rechargeCodeEntity.tips = rechargeCodeTypeEntity.tips;
                    }
                }
            }
            
            ListEntity listEntity = new ListEntity();
            listEntity.list = resultList;
            listEntity.total = businessDaoMapper.getRechargeCodeUsedCount(paramEntity);
            return listEntity;
        } else {//转让出去的列表
            List<RechargeCodeTransferEntity> rechargeCodeTransfer = businessDaoMapper.getRechargeCodeTransfer(paramEntity);
            
            List<RechargeCodeEntity> resultList = new ArrayList();
            for (RechargeCodeTransferEntity item : rechargeCodeTransfer){
                List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeList(0, rechargeCodeTransfer.size(), 0, 0, NumberFormatUtils.integerFormat(paramEntity.status), paramEntity.rechargeBeginTimeLong,
                    paramEntity.rechargeEndTimeLong, item.recharge_code, null, null, null, null, paramEntity.sellStatus, paramEntity.rechargeStatus, paramEntity.sellBeginTimeLong, paramEntity.sellEndTimeLong);
                if(rechargeCodeEntities.size() > 0){
                    resultList.add(rechargeCodeEntities.get(0));
                }
            }
            
            //过滤childProxy条件
            if( ! StringUtils.isEmpty(paramEntity.childProxyName)){
                List<RechargeCodeEntity> removeList = new ArrayList();
                for(RechargeCodeEntity item : resultList){
                    if( ! item.from_proxy_name.contains(paramEntity.childProxyName)){
                        removeList.add(item);
                    }
                }
                resultList.removeAll(removeList);
            }
            
            //数据补全
            for(RechargeCodeEntity item : resultList){
                for (RechargeCodeTransferEntity rechargeCodeTransferEntity : rechargeCodeTransfer){
                    if(item.recharge_code.equals(rechargeCodeTransferEntity.recharge_code)){
                        item.make_over_time = rechargeCodeTransferEntity.transfer_time;
                    }
                }
            }
            
            //类型tips补全
            List<RechargeCodeTypeEntity> rechargeCodeTypeList = getRechargeCodeTypeList();
            for (RechargeCodeEntity rechargeCodeEntity : resultList){
                for (RechargeCodeTypeEntity rechargeCodeTypeEntity : rechargeCodeTypeList){
                    if(rechargeCodeEntity.recharge_type == rechargeCodeTypeEntity.type){
                        rechargeCodeEntity.tips = rechargeCodeTypeEntity.tips;
                    }
                }
            }

            ListEntity listEntity = new ListEntity();
            listEntity.list = resultList;
            listEntity.total = businessDaoMapper.getRechargeCodeTransferCount(paramEntity);
            return listEntity;
        }
    }

    /**
     * 标记出售充值码
     */
    public int sellRechargeCodeSign(String proxyName, String[] rechargeCodes, String sellRemark) {
        //校验充值码
        for (String item : rechargeCodes){
            List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeList(0, 1, 0, 0, -1, 0, 0, item, null, proxyName, null, null, "0", null, 0, 0);
            if(rechargeCodeEntities.size() == 0){
                return 1;//充值码不存在
            }
            if(rechargeCodeEntities.get(0).status == 1){
                return 2; //充值码已被冻结
            }
            if(rechargeCodeEntities.get(0).status == 2){
                return 3; //充值码已被冻结
            }
        }
        
        for (String rechargeCode : rechargeCodes){
            businessDaoMapper.sellRechargeCodeSign(new Date().getTime(),rechargeCode,sellRemark);
        }
        return 4;//出售成功
    }

    /**
     * 出售充值码
     */
    public void sellRechargeCode(String[] rechargeCodes, String toUserName) {
        //获取用户信息
        List<UserEntity> userEntities = proxyUserDaoMapper.queryUserListByInfo(0, 1, 0, 0, toUserName, null, -1);
        
        for (String rechargeCode : rechargeCodes){
            businessDaoMapper.sellRechargeCode(new Date().getTime(),rechargeCode,userEntities.get(0).user_name,userEntities.get(0).id);
        }
    }
    
}
