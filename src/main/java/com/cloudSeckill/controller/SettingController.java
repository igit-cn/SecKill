package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.request.SettingInitRequest;
import com.cloudSeckill.data.request.SettingSaveRequest;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.utils.SessionUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SettingController extends BaseController {

    @Autowired private UserMapper userMapper;
    @Autowired private ReceiveDataControllerDll receiveDataController;

    @RequestMapping(value = "/settings", produces = "text/plain;charset=UTF-8")
    public String interceptor(HttpServletRequest request, HttpSession session) {
        if (SessionUtils.getUserInfo(session) != null) {
            return "settings";
        }
        return "redirect:index";
    }
    
    @RequestMapping(value = "/settings/init", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean init(@RequestBody SettingInitRequest request) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(request.id);
        List<User> userList = userMapper.selectByExample(example);
        return resultResponseSuccessObj(new Gson().toJson(userList.get(0)));
    }

    @RequestMapping(value = "/settings/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean save(@RequestBody SettingSaveRequest request) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(request.id);
        List<User> userList = userMapper.selectByExample(example);
        User user = userList.get(0);
        user.setPickType(request.pickType);
        user.setPickDelay(request.pickDelay);
        user.setPickDelayTime(request.pickDelayTime);
        user.setAutoPickPersonal(request.autoPickPersonal);
        user.setAutoReceiveTransfer(request.autoReceiveTransafer);
        userMapper.updateByExample(user, example);
        //更新轮询配置
        receiveDataController.addToken(user);
        return resultResponseSuccessObj("成功");
    }

}
