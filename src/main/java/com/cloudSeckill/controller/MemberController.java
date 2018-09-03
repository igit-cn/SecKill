package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.request.ConfirmMemberRequest;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.service.HomeService;
import com.cloudSeckill.service.UserService;
import com.cloudSeckill.utils.SessionUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class MemberController extends BaseController {

    @Autowired HomeService homeService;
    @Autowired UserService userService;
    @Autowired UserMapper userMapper;

    @RequestMapping(value = "/member", produces = "text/plain;charset=UTF-8")
    public String interceptor(HttpSession session) {
        if (SessionUtils.getUserInfo(session) != null) {
            //已经登录了
            return "member";
        }
        return "redirect:index";
    }
    
    @RequestMapping(value = "/member/getAllMember", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean getAllMember(HttpSession session) {
        UserExample example = new UserExample();
        UserInfo userInfo = SessionUtils.getUserInfo(session);
        example.createCriteria().andFromUserNameEqualTo(userInfo.userName);
        List<User> userList = userMapper.selectByExample(example);
        return resultResponseSuccessObj(new Gson().toJson(userList));
    }

    @RequestMapping(value = "/member/confirm", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean confirm(@RequestBody ConfirmMemberRequest request, HttpSession session) {
        String memberArrayStr = request.memberArrayStr;
        String[] split = memberArrayStr.split(",");
        Integer[] list = new Integer [split.length];
        for (int i = 0; i < split.length; i++) {
            list[i] = Integer.parseInt(split[i]);
        }
        UserExample example = new UserExample();
        example.createCriteria().andIdIn(Arrays.asList(list));
        List<User> userList = userMapper.selectByExample(example);
        session.setAttribute(SessionUtils.MEMBER_LIST, userList);

        return resultResponseSuccessObj("");
    }

}
