package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.RedPacket;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.mapper.RedPacketMapper;
import com.cloudSeckill.data.request.SearchGroupRequest;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.data.response.SearchGroupResponse;
import com.cloudSeckill.utils.SessionUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController extends BaseController {
    
    @Autowired private RedPacketMapper redPacketMapper;

    @RequestMapping(value = "/search", produces = "text/plain;charset=UTF-8")
    public String search(HttpSession session) {
        if (SessionUtils.getUserInfo(session) != null) {
            // 已经登录了
            return "search";
        }
        return "redirect:index";
    }

    
    @RequestMapping(value = "/search/initMember", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean searchGroup(HttpSession session) {
        List<User> memberList = SessionUtils.getMemberList(session);
        if (memberList != null) {
            return resultResponseSuccessObj(new Gson().toJson(memberList));
        } else {
            return resultResponseSuccessObj("");
        }
    }
    
    @RequestMapping(value = "/search/searchGroup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseBean searchGroup(@RequestBody SearchGroupRequest request, HttpSession session) {
        List<User> memberList = SessionUtils.getMemberList(session);

        if (memberList == null || memberList.size() <= 0) {
            return resultResponseErrorObj("请选择成员");
        }
        
        RedPacket redPacket = new RedPacket();
        for (int i = 0; i < memberList.size(); i++) {
            redPacket.userIdList.add(memberList.get(i).getId());
        }
        redPacket.beginPacketTime = request.startTime;
        redPacket.endPacketTime = request.endTime;
        List<RedPacket> redPackets = redPacketMapper.selectRedPacketByDateAndWechatId(redPacket);
        
        Map<String,SearchGroupResponse> responsesMap = new HashMap<>();
        for (RedPacket packetItem : redPackets){
            SearchGroupResponse searchGroupResponse = responsesMap.get(packetItem.group_id);
            if(searchGroupResponse == null){
                searchGroupResponse = new SearchGroupResponse();
                searchGroupResponse.count ++;
                searchGroupResponse.money = packetItem.money;
                searchGroupResponse.groupName = packetItem.group_name;
                searchGroupResponse.groupId = packetItem.group_id;
                responsesMap.put(packetItem.group_id,searchGroupResponse);
            } else {
                searchGroupResponse.groupId = packetItem.group_id;
                searchGroupResponse.count ++;
                searchGroupResponse.money += packetItem.money;
                if( ! StringUtils.isEmpty(packetItem.group_name) && ! "未命名".equals(packetItem.group_name)){
                    searchGroupResponse.groupName = packetItem.group_name;
                }
            }
        }
        
        List<SearchGroupResponse> responses = new ArrayList();
        for (String key : responsesMap.keySet()){
            responses.add(responsesMap.get(key));
        }
        
        return resultResponseSuccessObj(new Gson().toJson(responses));
    }
}
