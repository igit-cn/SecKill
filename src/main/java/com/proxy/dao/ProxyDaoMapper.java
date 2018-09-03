package com.proxy.dao;

import com.proxy.entity.ProxyInfoEntity;

import java.util.List;

public interface ProxyDaoMapper {

    /** 根据账号密码查询登录结果 */
    ProxyInfoEntity queryProxyInfoByAccountAndPassword(String account, String password);

    /** 根据账号名字查询信息  */
    ProxyInfoEntity queryProxyInfoByAccount(String account);

    /** 创建一个代理账号 */
    void createProxyAccount(String account, String password, String previousProxyAccount, int previousProxyId, String email, long createTime);

    /** 修改密码 */
    void alterProxyPassword(String account, String newPassword);

    /** 通过邮箱来获取代理信息 */
    ProxyInfoEntity queryProxyInfoByEmail(String email);

    /** 指定父代理名字查询旗下的子代理列表(分页)  */
    List<ProxyInfoEntity> queryProxyListByPreviousProxyName(String previousProxyName, int page, int size);

    /** 查询代理列表 (分页) */
    List<ProxyInfoEntity> queryProxyList(int page, int size);
    
    /** 查询代理列表总数 */
    long getProxyListCount();
    
    /** 根据条件查询代理列表总数 */
    long getProxyListCountByInfo(long beginTime, long endTime, String previousProxyName, String proxyName, int status, String proxyLevel);

    /** 查询代理列表根据条件 (分页) */
    List<ProxyInfoEntity> queryProxyListByInfo(int page, int size, long beginTimeLong, long endTimeLong, String previousProxyName, String proxyName, int status, String proxyLevel);

    /** 修改代理状态 */
    int resetProxyStatus(String[] proxyId, int status);

    /** 修改代理为有子代理 */
    void alterChildExist(int proxyId);
    
    /** 修改代理等级 */
    void updateProxyLevel(int proxyLevel, String proxyName);
}
