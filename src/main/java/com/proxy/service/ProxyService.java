package com.proxy.service;

import com.proxy.dao.ProxyDaoMapper;
import com.proxy.dao.ProxyListDaoMapper;
import com.proxy.entity.ProxyInfoEntity;
import com.proxy.entity.ProxyListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ProxyService {

    @Autowired private ProxyDaoMapper proxyDaoMapper;
    @Autowired private ProxyListDaoMapper proxyListDaoMapper;
    
    /**
     * 查询账号密码匹配结果 (校验登录)
     */
    public ProxyInfoEntity getProxyInfoByAccountAndPassword(String account, String password) {
        return proxyDaoMapper.queryProxyInfoByAccountAndPassword(account, password);
    }

    /**
     * 查询代理数量
     */
    public long getProxyListCount(){
        return proxyDaoMapper.getProxyListCount();
    }
    
    /**
     * 查询上级代理是否存在,存在则获取代理信息
     */
    public ProxyInfoEntity getProxyInfoByAccount(String account) {
        return proxyDaoMapper.queryProxyInfoByAccount(account);
    }

    /**
     * 创建一个代理账号
     */
    public void createProxyAccount(String account, String password, String previousProxyAccount, int previousProxyId, String email) {
        proxyDaoMapper.createProxyAccount(account, password, previousProxyAccount, previousProxyId, email, new Date().getTime());
        createProxyListMapper(previousProxyId,account);
    }
    
    /**
     * 生成代理层级映射
     */
    private void createProxyListMapper(int previousProxyId,String proxyName){
        
        ProxyInfoEntity proxyInfoEntity = proxyDaoMapper.queryProxyInfoByAccount(proxyName);
        proxyListDaoMapper.alterChildExist(previousProxyId);
        List<ProxyListEntity> proxyListMapper = proxyListDaoMapper.getProxyParentListById(previousProxyId);
        Collections.sort(proxyListMapper, new Comparator<ProxyListEntity>() {
            public int compare(ProxyListEntity o1, ProxyListEntity o2) {
                return o1.proxy_level > o2.proxy_level ? 1 : -1;
            }
        });
        
        if(proxyListMapper.size() > 0){
            proxyDaoMapper.alterChildExist(proxyInfoEntity.previous_proxy_id);
        }
        
        //修改映射名字和id并插入数据库
        for (ProxyListEntity listEntity : proxyListMapper){//批量插入生成数据
            listEntity.mapper_proxy_id = proxyInfoEntity.id;
            listEntity.mapper_proxy_name = proxyInfoEntity.proxy_name;
            listEntity.child_exist = 1;
            proxyListDaoMapper.insertProxyListMapper(listEntity);
        }
        
        ProxyListEntity currentProxyListEntity = new ProxyListEntity();
        currentProxyListEntity.mapper_proxy_id = proxyInfoEntity.id;
        currentProxyListEntity.mapper_proxy_name = proxyInfoEntity.proxy_name;
        currentProxyListEntity.proxy_id = proxyInfoEntity.id;
        currentProxyListEntity.proxy_name = proxyInfoEntity.proxy_name;
        currentProxyListEntity.parent_proxy_id = proxyInfoEntity.previous_proxy_id;
        currentProxyListEntity.parent_proxy_name = proxyInfoEntity.previous_proxy_name;
        currentProxyListEntity.proxy_level = proxyListMapper.size() + 1;
        currentProxyListEntity.register_time = proxyInfoEntity.register_time;
        proxyListDaoMapper.insertProxyListMapper(currentProxyListEntity);

        proxyDaoMapper.updateProxyLevel(proxyListMapper.size() + 1,proxyName);
    }
    
    /**
     * 修改代理账号的密码
     */
    public void alterPassword(String account, String newPassword) {
        proxyDaoMapper.alterProxyPassword(account, newPassword);
    }

    /**
     * 通过邮箱查询代理信息
     */
    public ProxyInfoEntity getProxyInfoByEmail(String email) {
        return proxyDaoMapper.queryProxyInfoByEmail(email);
    }

    /**
     * 指定父代理名字查询旗下的子代理列表(分页)
     */
    public List queryProxyListByPreviousProxyName(String previousProxyName, int page, int size) {
        return proxyDaoMapper.queryProxyListByPreviousProxyName(previousProxyName,page - 1,size);
    }
    /**
     * 查询代理列表(分页)
     */
    public List<ProxyInfoEntity> queryProxyList(int page, int size) {
        return proxyDaoMapper.queryProxyList(page - 1,size);
    }


    /**
     * 条件查询代理数量
     */
    public long getProxyListCountByInfo(String beginTime, String endTime, String previousProxyName, String proxyName, String status, String proxyLevel){
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
        } catch (Exception e){};
        return proxyDaoMapper.getProxyListCountByInfo(beginTimeLong,endTimeLong,previousProxyName,proxyName,statusInt,proxyLevel);
    }
    /**
     * 查询代理列表根据指定条件
     */
    public List<ProxyInfoEntity> queryProxyListByInfo(int page, int size, String beginTime, String endTime, String previousProxyName, String proxyName, String status, String proxyLevel) {
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
        } catch (Exception e) {};
        return proxyDaoMapper.queryProxyListByInfo(page - 1,size,beginTimeLong,endTimeLong,previousProxyName,proxyName,statusInt,proxyLevel);
    }

    public List<ProxyListEntity> getProxyParentListById(int proxyId){
        List<ProxyListEntity> proxyListMapper = proxyListDaoMapper.getProxyParentListById(proxyId);
        Collections.sort(proxyListMapper, new Comparator<ProxyListEntity>() {
            public int compare(ProxyListEntity o1, ProxyListEntity o2) {
                return o1.proxy_level > o2.proxy_level ? 1 : -1;
            }
        });
        
        ProxyListEntity proxyListEntity = new ProxyListEntity();
        proxyListEntity.proxy_name = "root";
        proxyListEntity.proxy_id = 1;
        proxyListEntity.proxy_level = 0;
        proxyListEntity.child_exist = 1;
        proxyListMapper.add(0,proxyListEntity);
        return proxyListMapper;
    }
    
    public List<ProxyListEntity> queryProxyParentListByName(String proxyName){
        List<ProxyListEntity> proxyListMapper = proxyListDaoMapper.getProxyParentListByName(proxyName);
        Collections.sort(proxyListMapper, new Comparator<ProxyListEntity>() {
            public int compare(ProxyListEntity o1, ProxyListEntity o2) {
                return o1.proxy_level > o2.proxy_level ? 1 : -1;
            }
        });
        
        ProxyListEntity proxyListEntity = new ProxyListEntity();
        proxyListEntity.proxy_name = "root";
        proxyListEntity.proxy_id = 1;
        proxyListEntity.proxy_level = 0;
        proxyListMapper.add(0,proxyListEntity);
        return proxyListMapper;
    }

    /**
     * 修改代理状态
     */
    public int resetProxyStatus(String[] proxyIds, int status) {
        return proxyDaoMapper.resetProxyStatus(proxyIds,status);
    }
}
