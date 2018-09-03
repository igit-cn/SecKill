package com.cloudSeckill.utils;

import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.request.LoginRequest;
import com.cloudSeckill.data.request.RegisterRequest;
import org.springframework.util.StringUtils;

public class DataValidateUtils {
    
    private boolean validateResult;
    private String validateMsg;
    /**
     * 登录接口参数校验
     */
    public static DataValidateUtils validateLoginParam(LoginRequest loginRequest){
        DataValidateUtils dataValidateUtils = new DataValidateUtils();
        dataValidateUtils.validateResult = false;
        if (loginRequest == null) {
            dataValidateUtils.validateMsg = "数据不正确";
        } else if (StringUtils.isEmpty(loginRequest.userName)) {
            dataValidateUtils.validateMsg = "请输入用户名";
        } else if (StringUtils.isEmpty(loginRequest.userPass)) {
            dataValidateUtils.validateMsg = "请输入密码";
        } else {
            dataValidateUtils.validateResult = true;
        }
        return dataValidateUtils;
    }
    /**
     * 注册接口参数校验
     */
    public static DataValidateUtils validateRegisterParam(RegisterRequest registerRequest){
        DataValidateUtils dataValidateUtils = new DataValidateUtils();
        dataValidateUtils.validateResult = false;
        //用户验证
        if (registerRequest == null) {
            dataValidateUtils.validateMsg = "数据不正确";
        } else if (StringUtils.isEmpty(registerRequest.userName)) {
            dataValidateUtils.validateMsg = "请输入用户名";
        } else if (StringUtils.isEmpty(registerRequest.userPass)) {
            dataValidateUtils.validateMsg = "请输入密码";
        } else if(StringUtils.isEmpty(registerRequest.userActiveCode)){
            dataValidateUtils.validateMsg = "请输入激活码";
        } else if(StringUtils.isEmpty(registerRequest.userEmail)){
            dataValidateUtils.validateMsg = "请输入邮箱";
        } else if(StringUtils.isEmpty(registerRequest.validateCode)){
            dataValidateUtils.validateMsg = "请输入验证码";
        } else {
            dataValidateUtils.validateResult = true;
        }
        return dataValidateUtils;
    }

    /**
     * 修改密码
     */
    public static DataValidateUtils validateResetPasswordParam(UserInfo userInfo){
        DataValidateUtils dataValidateUtils = new DataValidateUtils();
        dataValidateUtils.validateResult = false;
        //用户验证
        if (userInfo == null) {
            dataValidateUtils.validateMsg = "数据不正确";
        } else if (StringUtils.isEmpty(userInfo.userName)) {
            dataValidateUtils.validateMsg = "请输入用户名";
        } else if (StringUtils.isEmpty(userInfo.userPass)) {
            dataValidateUtils.validateMsg = "请输入密码";
        } else if(StringUtils.isEmpty(userInfo.emailVerifyCode)){
            dataValidateUtils.validateMsg = "请输入邮箱验证码";
        } else if(StringUtils.isEmpty(userInfo.validateCode)){
            dataValidateUtils.validateMsg = "请输入验证码";
        } else {
            dataValidateUtils.validateResult = true;
        }
        return dataValidateUtils;
    }

    public boolean isValidateResult() {
        return validateResult;
    }

    public String getValidateMsg() {
        return validateMsg;
    }
}
