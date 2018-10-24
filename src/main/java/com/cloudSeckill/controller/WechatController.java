package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.request.WechatLogoutRequest;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.service.WechatApiService;
import com.cloudSeckill.service.WechatServiceDll;
import com.cloudSeckill.service.WechatServiceJson;
import com.cloudSeckill.service.WechatServiceSocket;
import com.cloudSeckill.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class WechatController extends BaseController {

    @Autowired private WechatServiceDll wechatService;
    @Autowired private UserMapper userMapper;

    //获取登录需要的二维码地址
    @RequestMapping("wechat/getQRCode")
    public void getLoginQRCode(HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        session.setAttribute(SessionUtils.KENG_ID,httpServletRequest.getParameter("index"));
        byte[] bytes = wechatService.initWechatClient(session,SessionUtils.getUserInfo(session));
        if(bytes == null || bytes.length == 0){
            return;
        }
        //阻止生成的页面内容被缓存，保证每次重新生成验证码
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");//表明生成的响应是图片
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //取消轮询
    @RequestMapping("wechat/cancelLooperQRCodeStatus")
    public @ResponseBody ResponseBean cancelLooperQRCodeStatus(HttpSession session){
        UserInfo userInfo = SessionUtils.getUserInfo(session);
        userInfo.isLooperOpen = false;
        return resultResponseErrorObj("关闭轮询成功");
    }
    
    /**
     * 退出微信账号绑定
     */
    @RequestMapping("wechat/logout")
    public @ResponseBody ResponseBean logoutWX(@RequestBody WechatLogoutRequest wechatLogoutRequest) {
        UserExample queryExample = new UserExample();
        queryExample.createCriteria().andWechatIdEqualTo(wechatLogoutRequest.wechatId);
        User user = userMapper.selectByExample(queryExample).get(0);
        wechatService.wechatLogout(user);
        return resultResponseSuccessObj("退出登录成功");
    }
}
