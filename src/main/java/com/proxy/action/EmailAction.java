package com.proxy.action;

import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.NullEntity;
import com.proxy.entity.ProxyInfoEntity;
import com.proxy.entity.ResultEntity;
import com.proxy.service.EmailService;
import com.proxy.service.ProxyService;
import com.proxy.service.ProxyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("api")
public class EmailAction {

    @Autowired private ProxyService proxyService;
    @Autowired private EmailService emailService;
    @Autowired private ProxyUserService proxyUserService;

    /**
     * 发送邮箱验证码,用于找回代理
     */
    @RequestMapping("sendEmailCode") 
    public @ResponseBody
    String sendEmailCode(String email){
        ResultEntity resultEntity = new ResultEntity();
        if(StringUtils.isEmpty(email)){
            resultEntity.code = ResponseCode.DATA_ERROR;
            resultEntity.msg = "邮箱不能为空";
            return new Gson().toJson(resultEntity);
        }

        ProxyInfoEntity proxyInfoByEmail = proxyService.getProxyInfoByEmail(email);
        if(proxyInfoByEmail == null){
            resultEntity.code = ResponseCode.EMAIL_NOT_EXIST;
            resultEntity.msg = "邮箱不存在 ,请检查";
            return new Gson().toJson(resultEntity);
        }
        try {
            emailService.sendEmailCode(email);
        } catch (Exception e) {
            e.printStackTrace();
            resultEntity.code = ResponseCode.EMAIL_SEND_ERROR;
            resultEntity.msg = "邮件发送失败";
            return new Gson().toJson(resultEntity);
        }

        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "发送成功";
        return new Gson().toJson(resultEntity);
    }

    @ExceptionHandler(Exception.class)
    public void exception(RuntimeException e, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.DATA_ERROR;
        resultEntity.msg = "服务器错误";
        resultEntity.data = new NullEntity();
        PrintWriter writer = response.getWriter();
        writer.append(new Gson().toJson(resultEntity));
        writer.flush();
        writer.close();
    }
}
