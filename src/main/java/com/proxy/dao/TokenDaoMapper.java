package com.proxy.dao;

import com.proxy.entity.TokenEntity;

import java.util.List;

public interface TokenDaoMapper {

    void createToken(int proxyId, String proxyName, String token, long createTime);

    List<TokenEntity> checkToken(int proxyId, String proxyName, String token, long beforeTowHoursTime);
}
