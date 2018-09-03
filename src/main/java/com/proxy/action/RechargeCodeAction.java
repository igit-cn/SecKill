package com.proxy.action;

import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.*;
import com.proxy.service.RechargeCodeService;
import com.proxy.utils.CheckUtils;
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
public class RechargeCodeAction {
    
    @Autowired
    CheckUtils checkUtils;
    @Autowired
    RechargeCodeService rechargeCodeService;

    /**
     * 条件查询充值码列表 (分页)
     */
    @RequestMapping("queryRechargeCodeList")
    public @ResponseBody
    String queryRechargeCodeList(int page, int size, String token, String beginTime, String endTime, String status,
                                 String rechargeBeginTime, String rechargeEndTime, String rechargeCode, String rechargeCodeType, String rechargeUserName, String fromProxyName, String remark, String sellStatus, String rechargeStatus){
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.msg= "请求成功";
        resultEntity.code = ResponseCode.RESPONSE_OK;
        ListEntity listEntity = new ListEntity();
        listEntity.total = rechargeCodeService.queryRechargeCodeCount(beginTime,endTime,status,rechargeBeginTime,rechargeEndTime,rechargeCode,rechargeCodeType,rechargeUserName,fromProxyName,remark,sellStatus,rechargeStatus);
        listEntity.list = rechargeCodeService.queryRechargeCodeList(page,size,beginTime,endTime,status,rechargeBeginTime,rechargeEndTime,rechargeCode,rechargeCodeType,rechargeUserName,fromProxyName,remark,sellStatus,rechargeStatus);
        resultEntity.data = listEntity;
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 条件查询充值码列表 (分页)
     */
    @RequestMapping("queryRechargeCodeListByProxy")
    public @ResponseBody
    String queryRechargeCodeListByProxy(int page, int size, String token, String beginTime, String endTime, String status,
                                        String rechargeBeginTime, String rechargeEndTime, String rechargeCode, String rechargeCodeType, String rechargeUserName, String remark, String sellStatus, String rechargeStatus){
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkTokenReturnId(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.msg= "请求成功";
        resultEntity.code = ResponseCode.RESPONSE_OK;
        ListEntity listEntity = new ListEntity();
        listEntity.total = rechargeCodeService.queryRechargeCodeCount(beginTime,endTime,status,rechargeBeginTime,rechargeEndTime,rechargeCode,rechargeCodeType,rechargeUserName,proxyInfoEntity.proxy_name,remark,sellStatus,rechargeStatus);
        listEntity.list = rechargeCodeService.queryRechargeCodeList(page,size,beginTime,endTime,status,rechargeBeginTime,rechargeEndTime,rechargeCode,rechargeCodeType,rechargeUserName,proxyInfoEntity.proxy_name,remark,sellStatus,rechargeStatus);
        resultEntity.data = listEntity;
        return new Gson().toJson(resultEntity);
    }

    /**
     * root权限修改充值码状态
     */
    @RequestMapping("resetRechargeStatus")
    public @ResponseBody
    String resetRechargeStatus(String token , int status, @RequestParam("rechargeIds[]") String[] rechargeIds){
        String checkParams = checkUtils.checkParamsIsEmpty(rechargeIds);
        if(checkParams != null){
            return checkParams;
        }
        checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        String checkPermission= checkUtils.checkPermission(token);
        if(checkPermission != null){
            return checkPermission;
        }
        
        if(status < 0 || status > 2){//参数取值错误
            if(status == 5){//这个判断是兼容从前端那边传过来的错误类型
                status = 1;
            } else if(status == 6){
                status = 2;
            } else {
                throw new RuntimeException();
            }
        }
        
        rechargeCodeService.resetRechargeStatus(status, rechargeIds,0, 1, "root");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "修改成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }

    /**
     * 修改充值码状态
     */
    @RequestMapping("resetRechargeStatusByProxy")
    public @ResponseBody
    String resetRechargeStatusByProxy(String token , int status, String rechargeIds){
        String checkParams = checkUtils.checkParamsIsEmpty(rechargeIds,rechargeIds);
        if(checkParams != null){
            return checkParams;
        }
        checkParams = checkUtils.checkParamsIsEmpty(token);
        String[] rechargeIdsList = rechargeIds.split(",");
        if(checkParams != null || rechargeIdsList.length == 0){
            return checkParams;
        }
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkTokenReturnId(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }

        if(status < 0 || status > 2){//参数取值错误
            throw new RuntimeException();
        }
        
        ResultEntity resultEntity = new ResultEntity();
        
        //校验充值码从属关系,先获取充值码列表
        for (int i = 0; i < rechargeIdsList.length; i++) {
            RechargeCodeEntity rechargeCodeEntity = rechargeCodeService.queryRechargeCodeById(rechargeIdsList[i]);
            if(rechargeCodeEntity.status != 0 && proxyInfoEntity.proxy_level > rechargeCodeEntity.status_edit_level){
                //操作拒绝
                resultEntity.code = ResponseCode.PERMISSION_REFUSE;
                resultEntity.data = new NullEntity();
                resultEntity.msg = "您的充值码 " + rechargeCodeEntity.recharge_code + "操作权限被拒绝，上级操作代理商为：" + rechargeCodeEntity.status_edit_name;
                return new Gson().toJson(resultEntity);
            }
        }
        
        rechargeCodeService.resetRechargeStatus(status,rechargeIds.split(","),proxyInfoEntity.proxy_level,proxyInfoEntity.id,proxyInfoEntity.proxy_name);
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "修改成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }
    
    @ExceptionHandler(Exception.class)
    public void exception(RuntimeException e, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.setCharacterEncoding("UTF-8");
        httpServletRequest.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.data = new NullEntity();
        resultEntity.code = ResponseCode.DATA_ERROR;
        resultEntity.msg = "服务器错误";
        PrintWriter writer = response.getWriter();
        writer.append(new Gson().toJson(resultEntity));
        writer.flush();
        writer.close();
    }
}
