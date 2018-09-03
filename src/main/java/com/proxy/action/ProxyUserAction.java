package com.proxy.action;

import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.ListEntity;
import com.proxy.entity.NullEntity;
import com.proxy.entity.ResultEntity;
import com.proxy.service.ProxyUserService;
import com.proxy.service.RechargeCodeService;
import com.proxy.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("api")
public class ProxyUserAction {

    @Autowired CheckUtils checkUtils;
    @Autowired ProxyUserService proxyUserService;
    @Autowired RechargeCodeService rechargeCodeService;

    /**
     * 根据可选条件查询用户列表
     */
    @RequestMapping("queryUserListByInfo")
    public @ResponseBody String queryUserListByInfo(String token, int page, int size, String beginTime, String endTime, String userName, String remark, String status) {
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if (checkParams != null) {
            return checkParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if (checkPermission != null) {
            return checkPermission;
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "请求成功";
        ListEntity listEntity = new ListEntity();
        listEntity.total = proxyUserService.queryUserListCountByInfo(beginTime, endTime, userName, remark, status);
        listEntity.list = proxyUserService.queryUserListByInfo(page, size, beginTime, endTime, userName, remark, status);
        resultEntity.data = listEntity;
        return new Gson().toJson(resultEntity);
    }

    /**
     * 修改用户状态
     */
    @RequestMapping("resetUserStatus")
    public @ResponseBody
    String resetUserStatus(String token, @RequestParam(value = "userIds[]") String[] userIds, int status) throws Exception {
        String checkParams = checkUtils.checkParamsIsEmpty(userIds);
        if (checkParams != null) {
            return checkParams;
        }
        String checkTokenParams = checkUtils.checkParamsIsEmpty(token);
        if (checkTokenParams != null) {
            return checkTokenParams;
        }
        String checkPermission = checkUtils.checkPermission(token);
        if (checkPermission != null) {
            return checkPermission;
        }
        if (status < 0 || status > 2) {//参数取值错误
            throw new Exception();
        }

        proxyUserService.alterUserStatus(userIds, status);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "修改成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }

    @ExceptionHandler(Exception.class)
    public void exception(RuntimeException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.DATA_ERROR;
        resultEntity.data = new NullEntity();
        resultEntity.msg = "服务器错误";
        PrintWriter writer = response.getWriter();
        writer.append(new Gson().toJson(resultEntity));
        writer.flush();
        writer.close();
    }
}
