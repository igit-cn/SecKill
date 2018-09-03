package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.RedPacket;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.RedPacketMapper;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.request.QueryRechargeCodeRequest;
import com.cloudSeckill.data.request.RechargeRequest;
import com.cloudSeckill.data.request.RemoveItemRequest;
import com.cloudSeckill.data.response.ProxyResultBean;
import com.cloudSeckill.data.response.QueryRechargeCodeStatusBean;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.service.HomeService;
import com.cloudSeckill.service.UserService;
import com.cloudSeckill.service.WechatService;
import com.cloudSeckill.utils.SessionUtils;
import com.cloudSeckill.utils.TextUtils;
import com.google.gson.Gson;
import com.proxy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController extends BaseController {

    @Autowired private HomeService homeService;
    @Autowired private UserService userService;
    @Autowired private WechatService wechatService;
            
    @Autowired private UserMapper userMapper;
    @Autowired private RedPacketMapper redPacketMapper;
    @Autowired private ReceiveDataController receiveDataController;
    
    @RequestMapping(value = "/", produces = "text/plain;charset=UTF-8")
    public String interceptor(Model model,HttpSession session) {
        if (SessionUtils.getUserInfo(session) != null) {
            //已经登录了
            return pCenter(model,session);
        }
        return "redirect:index";
    }
    
    @RequestMapping(value = "/admin",produces = "text/plain;charset=UTF-8")
    public String interceptorAdmin(){
        return "redirect:admin/index.html";
    }
    
    @RequestMapping(value = "/mobile",produces = "text/plain;charset=UTF-8")
    public String interceptorMobile(){
        return "redirect:mobile/index.html";
    }

    @RequestMapping(value = "/query",produces = "text/plain;charset=UTF-8")
    public String interceptorSearch(){
        return "redirect:search/search.html";
    }
    /**
     * 查询当前在线人数
     */
    @RequestMapping("onlineUserNumber")
    public @ResponseBody ResponseBean getCurrentOnlineUserNumber(){
        return resultResponseSuccessObj("当前在线人数 : " + receiveDataController.getCurrentOnlineUserNumber());
    }
    /**
     *  查询当前充值码状态
     */
    @RequestMapping("queryRechargeCodeStatus")
    public @ResponseBody ResponseBean queryRechargeCodeStatus(@RequestBody QueryRechargeCodeRequest queryRechargeCodeRequest, HttpSession session){
        if (StringUtils.isEmpty(queryRechargeCodeRequest.rechargeCode)) {
            return resultResponseErrorObj("充值码不能为空");
        }
        if (StringUtils.isEmpty(queryRechargeCodeRequest.verifyCode)) {
            return resultResponseErrorObj("验证码不能为空");
        }
        //校验验证码
        if ( ! userService.checkVerifyCode(queryRechargeCodeRequest.rechargeCode, session)) {
            //校验验证码
            if ( ! userService.checkVerifyCode(queryRechargeCodeRequest.verifyCode, session)) {
                return resultResponseErrorObj("验证码错误");
            }
        }
        //查询充值吗
        QueryRechargeCodeStatusBean queryRechargeCodeStatusBean = homeService.queryRechargeCodeStatus(queryRechargeCodeRequest.rechargeCode);
        if(queryRechargeCodeStatusBean == null){
            return resultResponseErrorObj("充值码不存在");
        }
        return resultResponseSuccessObj(new Gson().toJson(queryRechargeCodeStatusBean));
    }
    
    //跳转到个人中心首页
    @RequestMapping(value = "/home", produces = "text/plain;charset=UTF-8")
    public String pCenter(Model model,HttpSession session) {
        int sum = 0;
        List<User> kengList = homeService.getKengList(SessionUtils.getUserInfo(session).userName);
        for (int i = 0; i < kengList.size(); i++) {
            //进入抢包统计
            RedPacket redPacket = new RedPacket();
            redPacket.user_id = kengList.get(i).getId();
            redPacket.setTodayPacketTime(new Date());
            int todaySum = redPacketMapper.selectRedPacketSumToday(redPacket);
            //今日总统计
            sum += todaySum;
            kengList.get(i).setTodaySum(todaySum);
            //在线离线更新
//            int status = wechatService.getUserStatusIsLogin(kengList.get(i));
//            kengList.get(i).setOnlineStatus(status);
//            if(status == 2){
//                receiveDataController.removeToken(kengList.get(i).getToken());
//            }
        }
        
        model.addAttribute("userInfo",new Gson().toJson(kengList));
        model.addAttribute("sum",sum);
        return "home";
    }
    
    /**
     * 生成新的坑
     */
    @RequestMapping(value = "/home/addItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean addItem() {
        User user = new User();
        user.setName("");
        user = homeService.addItem(user);
        return resultResponseSuccessObj(new Gson().toJson(user));
    }

    /**
     * 解绑
     */
    @RequestMapping(value = "/home/removeItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean removeItem(@RequestBody RemoveItemRequest request) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(request.id);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)) {
            return resultResponseSuccessObj("");
        }
        
        User user = userList.get(0);
        wechatService.wechatLogout(user);//服务器下线
        
        user.setWechatId("");
        user.setUserId("");
        user.setName("");
        user.setOnlineStatus(2);
        user.setHeadImg("");
        user.setIncome(0);
        userMapper.updateByExample(user, example);
        
        //接收数据端清除缓存token
        receiveDataController.removeToken(user.getWechatId());
        return resultResponseSuccessObj("");
    }

    /**
     * 充值
     */
    @RequestMapping("/home/rechargeCodeUp")
    public @ResponseBody ResponseBean rechargeCodeUp(@RequestBody RechargeRequest rechargeCode, HttpSession session) {
        if (TextUtils.isEmpty(rechargeCode.rechargeCode)) {
            return resultResponseErrorObj("请求参数不正确");
        }

        if (rechargeCode.rechargeCode == null || rechargeCode.rechargeCode.length() != 10) {
            return resultResponseErrorObj("充值码不存在");
        }
        
        String userName = SessionUtils.getUserInfo(session).userName;
        ProxyResultBean proxyResultBean = userService.rechargeCodeUp(userName, rechargeCode.id, rechargeCode.rechargeCode);
        if ( ! proxyResultBean.isResponseSuccess()) {
            return resultResponseErrorObj(proxyResultBean.msg);
        }
        
        //更新轮询队列到期时间
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andFromUserNameEqualTo(userName);
        List<User> users = userMapper.selectByExample(userExample);
        receiveDataController.addToken(users.get(0));
        
        return resultResponseSuccessObj(SessionUtils.getUserInfo(session).useTime + "");//返回剩余时间
    }
}
