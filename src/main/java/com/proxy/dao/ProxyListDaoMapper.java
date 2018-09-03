package com.proxy.dao;

import com.proxy.entity.ProxyListEntity;

import java.util.List;

public interface ProxyListDaoMapper {
    
    /** 根据子代理id查询所有父代理列表 */
    List<ProxyListEntity> getProxyParentListById(int proxyId);
    
    /** 根据子代理名字查询所有父代理列表 */
    List<ProxyListEntity> getProxyParentListByName(String proxyName);

    /** 插入代理账号层级映射 */
    void insertProxyListMapper(ProxyListEntity proxyListEntity);

    /** 修改代理为有子代理 */
    void alterChildExist(int proxyId);
}
