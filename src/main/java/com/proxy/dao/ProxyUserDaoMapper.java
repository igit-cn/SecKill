package com.proxy.dao;

import com.proxy.entity.UserEntity;

import java.util.List;

public interface ProxyUserDaoMapper {

    /**
     * 通过可选条件查询用户列表
     */
    List<UserEntity> queryUserListByInfo(int page, int size, long beginTimeLong, long endTimeLong, String userName, String remark, int statusInt) ;

    /**
     * 通过可选条件查询用户列表的总数量
     */
    int queryUserListCountByInfo(long beginTimeLong, long endTimeLong, String userName, String remark, int statusInt);

    /**
     * 创建用户信息
     */
    void createUserInfo(UserEntity userEntity);

    /**
     * 修改用户状态
     */
    void alterUserStatus(String[] userIds, int status);

    /**
     * 查询用户名和邮箱是否注册过
     */
    List<UserEntity> queryUserRegister(String userName, String email);

    /**
     * 查询用户名和邮箱是否注册过
     */
    List<UserEntity> queryUserRegisterByName(String userName);

    /**
     * 用户注册
     */
    void userRegister(String userName, String digest, String email, long coinCount, long time);

    /**
     * 通过邮箱查询用户信息
     */
    UserEntity getUserInfoByUserName(String userName);

    /**
     * 修改用户密码
     */
    void alterUserPassword(String user_name, String newPassword);

    /**
     * 校验用户名是否正确
     */
    UserEntity userLogin(String userName, String password);

    /**
     * 设置用户使用到期时间
     */
    void addUserUseTime(long time, String userName);
    
}
