package com.proxy.dao;

import com.proxy.entity.*;

import java.util.List;

public interface BusinessDaoMapper {

    /** 生成代理卡 */
    void pushRechargeCard(RechargeCodeEntity rechargeCodeEntity);
    
    /** 添加备注 */
    void addRemark(int id, String proxyName, String remark);

    /** 根据父代理查询子代理列表 */ 
    List<ProxyListEntity> queryProxyListByPreviousProxyName(String previous_proxy_name, int page, int size);
    
    /** 根据父代理查询子代理列表总数量 */
    long queryProxyListCountByPreviousProxyName(String previous_proxy_name);
    
    /** 指定代理商和充值码获取信息 */
    RechargeCodeTransferEntity queryRechargeByCodeWithToProxyName(String fromProxy, String rechargeCode);
    
    /** 代理商转卡给下级代理 */ 
    void makeOverRechargeCode(String rechargeCode, String fromProxyName, String toProxyName, String remark, long transferTime, int rechargeCodeType);

    /** 修改代理商转让充值码表详情中的从属代理商 */ 
    void makeOverRechargeCodeTableRechargeCode(String rechargeCode, String proxyName, int proxyId, long create_time);
    
    /** 获取充值码的所有类型 */
    List<RechargeCodeTypeEntity> getRechargeCodeTypeList();
    
    /** 获取父代理转让给子代理的所有充值码列表 */
    List<RechargeCodeTransferEntity> getMakeOverToChild(String fromProxyName, String toProxyName);

    /** 根据指定充值码类型获取当前剩余充值卡的数量 */
    int getSurplusRechargeCount(int rechargeCodeType, String proxyName);

    /** 根据充值码类型获取剩余充值码列表 */
    List<RechargeCodeEntity> getSurplusRechargeList(String proxyName, int type, int page, int size);
    long getSurplusRechargeListCount(String proxyName, int type);

    /** 获取已充值的充值码列表 */
    List<RechargeCodeEntity> getRechargeCodeUsed(RechargeCodeSellOrUsedParamEntity paramEntity);
    long getRechargeCodeUsedCount(RechargeCodeSellOrUsedParamEntity paramEntity);
    
    /** 获取充值码 */
    List<RechargeCodeTransferEntity> getRechargeCodeTransfer(RechargeCodeSellOrUsedParamEntity paramEntity);
    long getRechargeCodeTransferCount(RechargeCodeSellOrUsedParamEntity paramEntity);

    /** 出售充值码 */
    void sellRechargeCode(long sellTime, String rechargeCode, String userName, int userId);

    /** 出售充值码 */
    void sellRechargeCodeSign(long sellTime, String rechargeCode, String sellRemark);

}
