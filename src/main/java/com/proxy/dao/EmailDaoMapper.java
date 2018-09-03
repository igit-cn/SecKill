package com.proxy.dao;

import com.proxy.entity.EmailEntity;

import java.util.List;

public interface EmailDaoMapper {

    /**
     * 查询两小时内发送的邮件列表
     */
    List<EmailEntity> getInTwoHoursEmail(String email, long beforeTowHourTime);
    /**
     * 保存生成的邮件
     */
    void saveEmailCode(String email, int randomCode, long sendTime);
}
