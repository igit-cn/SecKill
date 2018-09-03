package com.proxy.action;

import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.ListEntity;
import com.proxy.entity.NullEntity;
import com.proxy.entity.ProxyInfoEntity;
import com.proxy.entity.ResultEntity;
import com.proxy.service.EmailService;
import com.proxy.service.ProxyService;
import com.proxy.service.TokenService;
import com.proxy.utils.CheckUtils;
import com.proxy.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("api")
public class ProxyAction {

    @Autowired ProxyService proxyService;
    @Autowired EmailService emailService;
    @Autowired TokenService tokenService;
    @Autowired CheckUtils checkUtils;
    
    /**
     * 后台boss管理端登录
     */
    @RequestMapping("rootManagerLogin")
    public @ResponseBody String rootManagerLogin(String account, String password) {
        String checkParams = checkUtils.checkParamsIsEmpty(account, password);
        if(checkParams != null){
            return checkParams;
        }

        ResultEntity resultEntity = new ResultEntity();
        ProxyInfoEntity proxyInfoEntity = proxyService.getProxyInfoByAccountAndPassword(account, MD5Util.digest(password));
        if ( ( ! "root".equals(account)) || proxyInfoEntity == null) {
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "账号或者密码错误，请核实后重试";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }

        //生成token
        proxyInfoEntity.token = tokenService.createToken(proxyInfoEntity.id, proxyInfoEntity.proxy_name);
        
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.data = proxyInfoEntity;
        resultEntity.msg = "登录成功";
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 登录
     */
    @RequestMapping("proxyLogin")
    public @ResponseBody String userList(String account, String password) {
        String checkParams = checkUtils.checkParamsIsEmpty(account, password);
        if(checkParams != null){
            return checkParams;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        
        if("root".equals(account)){
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "账号为后台管理系统账号,无法登录";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }

        ProxyInfoEntity proxyInfoEntity = proxyService.getProxyInfoByAccountAndPassword(account, MD5Util.digest(password));
        if (proxyInfoEntity == null) {
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "账号或者密码错误，请核实后重试";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        if(proxyInfoEntity.status == 1){
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "代理账号已被冻结";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        if(proxyInfoEntity.status == 2){
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "代理账号已被删除";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        //生成token
        proxyInfoEntity.token = tokenService.createToken(proxyInfoEntity.id, proxyInfoEntity.proxy_name);

        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.data = proxyInfoEntity;
        resultEntity.msg = "登录成功";
        return new Gson().toJson(resultEntity);
    }

    /**
     * 注册代理
     */
    @RequestMapping("registerProxy")
    public @ResponseBody String registerProxy(String account, String password, String previousProxyAccount, String email) {
        String checkParams = checkUtils.checkParamsIsEmpty(account,password,previousProxyAccount,email);
        if(checkParams != null){
            return checkParams;
        }

        ResultEntity resultEntity = new ResultEntity();
        ProxyInfoEntity firstPreviousInfoEntity = proxyService.getProxyInfoByAccount(previousProxyAccount);
        if (firstPreviousInfoEntity == null) {
            resultEntity.code = ResponseCode.PROXY_ACCOUNT_NOT_EXIST;
            resultEntity.msg = "上级代理账号不存在";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        
        if (proxyService.getProxyInfoByAccount(account) != null) {
            resultEntity.code = ResponseCode.CURRENT_PROXY_ACCOUNT_EXIST;
            resultEntity.msg = "当前账号已经被注册";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }

        if (proxyService.getProxyInfoByEmail(email) != null) {
            resultEntity.code = ResponseCode.CURRENT_PROXY_EMAIL_EXIST;
            resultEntity.msg = "当前邮箱已经被注册";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }

        proxyService.createProxyAccount(account, MD5Util.digest(password), firstPreviousInfoEntity.proxy_name, firstPreviousInfoEntity.id, email);
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "注册成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }

    /**
     * 修改密码
     */
    @RequestMapping("alterPassword")
    public @ResponseBody String alterPassword(String account, String password, String newPassword){
        String checkParams = checkUtils.checkParamsIsEmpty(account,password,newPassword);
        if(checkParams != null){
            return checkParams;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        ProxyInfoEntity proxyInfoEntity = proxyService.getProxyInfoByAccountAndPassword(account, MD5Util.digest(password));
        if(proxyInfoEntity == null){
            resultEntity.code = ResponseCode.LOGIN_ERROR;
            resultEntity.msg = "账号或者密码错误，请核实后重试";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }

        proxyService.alterPassword(account,MD5Util.digest(newPassword));

        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "修改成功";
        resultEntity.data = proxyInfoEntity;
        return new Gson().toJson(resultEntity);
    }

    /**
     * 查询代理列表  (废弃 屏蔽)
     */
    public @ResponseBody String queryProxyList(int page, int size, String token){
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "查询成功";
        ListEntity listEntity = new ListEntity();
        listEntity.total = proxyService.getProxyListCount();
        listEntity.list = proxyService.queryProxyList(page,size);
        resultEntity.data = listEntity;

        return new Gson().toJson(resultEntity);
    }
  
//    /**
//     * 指定父代理名字查询旗下的子代理列表(分页)
//     */
//    @RequestMapping("queryProxyListByPreviousProxyName")
//    public @ResponseBody String queryProxyListByPreviousProxyName(String previousProxyName,int page,int size,String token){
//        String checkParams = checkUtils.checkParamsIsEmpty(previousProxyName, token);
//        if(checkParams != null){
//            return checkParams;
//        }
//        String checkPermission = checkUtils.checkPermission(token);
//        if(checkPermission != null){
//            return checkPermission;
//        }
//        
//        ResultEntity resultEntity = new ResultEntity();
//        ProxyInfoEntity firstPreviousInfoEntity = proxyService.getProxyInfoByAccount(previousProxyName);
//        if (firstPreviousInfoEntity == null) {
//            resultEntity.code = ResponseCode.PROXY_ACCOUNT_NOT_EXIST;
//            resultEntity.msg = "上级代理账号不存在";
//            resultEntity.data = new NullEntity();
//            return new Gson().toJson(resultEntity);
//        }
//        
//        resultEntity.code = ResponseCode.RESPONSE_OK;
//        resultEntity.msg = "查询成功";
//        ListEntity listEntity = new ListEntity();
//        listEntity.total = proxyService.getProxyListCountByInfo(null,null,previousProxyName,null,null);
//        listEntity.list = proxyService.queryProxyListByPreviousProxyName(previousProxyName,page,size);
//        resultEntity.data = listEntity;
//        return new Gson().toJson(resultEntity);
//    }

    /**
     * 根据条件查询代理列表 (分页)
     */
    @RequestMapping("queryProxyListByInfo")
    public @ResponseBody String queryProxyListByInfo(int page, int size, String proxyLevel, String beginTime, String endTime, String previousProxyName, String status, String proxyName, String token){
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "查询成功";
        ListEntity listEntity = new ListEntity();
        listEntity.total = proxyService.getProxyListCountByInfo(beginTime,endTime,previousProxyName,proxyName,status,proxyLevel);
        listEntity.list = proxyService.queryProxyListByInfo(page,size,beginTime,endTime,previousProxyName,proxyName,status,proxyLevel);
        resultEntity.data = listEntity;
        
        return new Gson().toJson(resultEntity);
    }

    /**
     * 根据名字查询指定代理
     */
    @RequestMapping("queryProxyByName")
    public @ResponseBody String queryProxyByName(String proxyName, String token){
        String checkParams = checkUtils.checkParamsIsEmpty(proxyName, token);
        if(checkParams != null){
            return checkParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "查询成功";
        resultEntity.data = proxyService.getProxyInfoByAccount(proxyName);
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 批量修改代理状态
     */
    @RequestMapping("resetProxyStatus")
    public @ResponseBody String resetProxyStatus(@RequestParam(value="proxyIds[]") String[] proxyIds, String token, int status) throws Exception {
        String checkParams = checkUtils.checkParamsIsEmpty(proxyIds);
        if(checkParams != null){
            return checkParams;
        }
        String checkTokenParams = checkUtils.checkParamsIsEmpty(token);
        if(checkTokenParams != null){
            return checkTokenParams;
        }
        if(status < 0 || status > 2){//参数取值错误
            throw new Exception();
        }
        String checkPermission = checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }
        
        ResultEntity resultEntity = new ResultEntity();
        if (proxyService.resetProxyStatus(proxyIds,status) == 0) {
            resultEntity.code = ResponseCode.PROXY_ACCOUNT_NOT_EXIST;
            resultEntity.msg = "代理账号不存在";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "状态更新成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 根据指定代理id查询其父代理树状结构列表
     */
    @RequestMapping("queryProxyParentList")
    public @ResponseBody String queryProxyParentList(int proxyId){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.data = proxyService.getProxyParentListById(proxyId);
        resultEntity.msg = "请求成功";
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 根据指定代理id查询其父代理树状结构列表
     */
    @RequestMapping("queryProxyParentListByName")
    public @ResponseBody String queryProxyParentListByName(String proxyName){
        String checkParamsIsEmpty = checkUtils.checkParamsIsEmpty(proxyName);
        if(checkParamsIsEmpty != null){
            return checkParamsIsEmpty;
        }
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.data = proxyService.queryProxyParentListByName(proxyName);
        resultEntity.msg = "请求成功";
        return new Gson().toJson(resultEntity);
    }
    
    @ExceptionHandler(Exception.class)
    public void exception(RuntimeException e, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        httpServletRequest.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.DATA_ERROR;
        resultEntity.data = new NullEntity();
        resultEntity.msg = "服务器错误 : " + e.toString();
        PrintWriter writer = response.getWriter();
        writer.append(new Gson().toJson(resultEntity));
        writer.flush();
        writer.close();
    }
}




