package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.request.LoginRequest;
import com.cloudSeckill.data.request.RegisterRequest;
import com.cloudSeckill.data.request.SendEmailRequest;
import com.cloudSeckill.data.response.HomeUserInfoBean;
import com.cloudSeckill.data.response.ProxyResultBean;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.service.UserService;
import com.cloudSeckill.utils.DataValidateUtils;
import com.cloudSeckill.utils.SessionUtils;
import com.cloudSeckill.utils.TextUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户相关操作
 */
@Controller
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @RequestMapping("login")
    public @ResponseBody ResponseBean login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        //校验数据
        DataValidateUtils dataValidateUtils = DataValidateUtils.validateLoginParam(loginRequest);
        if (!dataValidateUtils.isValidateResult()) {
            return resultResponseErrorObj(dataValidateUtils.getValidateMsg());
        }

        //登录
        UserInfo userInfo = userService.login(loginRequest, session);
        //有返回的异常信息
        if (!TextUtils.isEmpty(userInfo.server_msg)) {
            return resultResponseErrorObj(userInfo.server_msg);
        }
        return resultResponseSuccessObj("登录成功");
    }

    /**
     * 生成验证码,并存入redis中
     */
    @RequestMapping("verifyCode") 
    public @ResponseBody ResponseBean createVerifyCode(HttpSession session) {
        String verifyCode = userService.createVerifyCode(session);
        return resultResponseSuccessObj(verifyCode);
    }

    /**
     * 用户注册
     */
    @RequestMapping("register")
    public @ResponseBody ResponseBean register(@RequestBody RegisterRequest registerRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return resultResponseErrorObj(bindingResult.getFieldError().getDefaultMessage());
        }
        //校验数据
        DataValidateUtils dataValidateUtils = DataValidateUtils.validateRegisterParam(registerRequest);
        if (!dataValidateUtils.isValidateResult()) {
            return resultResponseErrorObj(dataValidateUtils.getValidateMsg());
        }
        //校验验证码
        if (!userService.checkVerifyCode(registerRequest.validateCode, session)) {
            return resultResponseErrorObj("验证码错误");
        }
        ProxyResultBean proxyResultBean = userService.UserRegister(registerRequest);
        if (!proxyResultBean.isResponseSuccess()) {
            return resultResponseErrorObj(proxyResultBean.msg);
        }
        return resultResponseSuccessObj(proxyResultBean.msg);
    }

    /**
     * 发送邮箱验证码
     */
    @RequestMapping("sendEmailCode")
    public @ResponseBody ResponseBean sendEmailCode(@RequestBody SendEmailRequest sendEmailRequest) {
        if (sendEmailRequest == null || StringUtils.isEmpty(sendEmailRequest.userName)) {
            return resultResponseErrorObj("用户名不能为空");
        }
        ProxyResultBean proxyManagerUserResponse = userService.sendEmailCode(sendEmailRequest.userName);
        if (proxyManagerUserResponse == null) {
            return resultResponseErrorObj("服务器错误");
        }
        if (!proxyManagerUserResponse.isResponseSuccess()) {
            return resultResponseErrorObj(proxyManagerUserResponse.msg);
        }
        return resultResponseSuccessObj(proxyManagerUserResponse.msg);
    }

    /**
     * 重置用户密码
     */
    @RequestMapping("resetPassword")
    public @ResponseBody ResponseBean resetPassword(@RequestBody UserInfo userInfo, HttpSession session) throws Exception {
        //校验数据
        DataValidateUtils dataValidateUtils = DataValidateUtils.validateResetPasswordParam(userInfo);
        if (!dataValidateUtils.isValidateResult()) {
            return resultResponseErrorObj(dataValidateUtils.getValidateMsg());
        }
        //校验验证码
        if (!userService.checkVerifyCode(userInfo.validateCode, session)) {
            return resultResponseErrorObj("验证码错误");
        }
        ProxyResultBean proxyResultBean = userService.resetPassword(userInfo);
        if (!proxyResultBean.isResponseSuccess()) {
            return resultResponseErrorObj(proxyResultBean.msg);
        }
        return resultResponseSuccessObj(proxyResultBean.msg);
    }
    
    /**
     * 登录成功后获取数据,缓存积分等
     */
    @RequestMapping("userInfo") 
    public @ResponseBody ResponseBean getUserInfo(HttpSession session) {
        UserInfo userInfo = SessionUtils.getUserInfo(session);
        if (userInfo == null || TextUtils.isEmpty(userInfo.userName)) {
            return resultResponseErrorObj("未登录");
        }
        return resultResponseSuccessObj(new Gson().toJson(new HomeUserInfoBean(userInfo, null)));
    }

    @ExceptionHandler(Exception.class)
    public void exception(RuntimeException e, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        httpServletRequest.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.append(new Gson().toJson(resultResponseErrorObj("服务器错误 : " + e.toString())));
        writer.flush();
        writer.close();
    }
}
