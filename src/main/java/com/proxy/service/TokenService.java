package com.proxy.service;

import com.proxy.dao.ProxyDaoMapper;
import com.proxy.dao.TokenDaoMapper;
import com.proxy.entity.ProxyInfoEntity;
import com.proxy.entity.TokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    TokenDaoMapper tokenDaoMapper;
    @Autowired
    ProxyDaoMapper proxyDaoMapper;

    /**
     * 根据账号名字和id生成token
     */
    public String createToken(int proxyId, String proxyName) {
        String token = UUID.randomUUID().toString();
        tokenDaoMapper.createToken(proxyId, proxyName, token, new Date().getTime());
        return token;
    }
    
    /**
     * 通过token返回代理信息
     */
    public ProxyInfoEntity checkTokenReturnProxy(int proxyId, String proxyName, String token){
        if(token == null){
            return null;
        }
        List<TokenEntity> tokenEntities = tokenDaoMapper.checkToken(proxyId, proxyName, token, new Date().getTime() - 1000L * 60 * 60 * 2);
        if(tokenEntities.size() == 0){
            return null;
        }
        tokenDaoMapper.createToken(tokenEntities.get(0).proxy_id, tokenEntities.get(0).proxy_name, token, new Date().getTime());
        return proxyDaoMapper.queryProxyInfoByAccount(tokenEntities.get(0).proxy_name);
    }
}
