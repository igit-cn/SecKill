package com.cloudSeckill.controller;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.dao.domain.RedPacket;
import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.RedPacketMapper;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.response.*;
import com.cloudSeckill.net.web_socket.WechatWebSocket;
import com.cloudSeckill.service.DllInterface;
import com.cloudSeckill.service.WechatServiceInter;
import com.cloudSeckill.utils.LogUtils;
import com.cloudSeckill.utils.RedisUtil;
import com.cloudSeckill.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proxy.utils.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Controller
public class ReceiveDataControllerDll extends BaseController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WechatServiceInter wechatServeice;
    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WechatWebSocket wechatWebSocket;

    private HashMap<String, User> tokenList = new HashMap(); // token 列表
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 获取当前在线人数
     */
    public int getCurrentOnlineUserNumber() {
        return tokenList.size();
    }

    /**
     * 数据接收通知
     */
    @RequestMapping(value = "/receive/notification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    //ResponseBean notification(String type, String WXUserId) {
    ResponseBean notification(HttpServletRequest request) {
        String str = "";
        if (request.getParameterNames().hasMoreElements()) {
            str = request.getParameterNames().nextElement();
        }
        if (TextUtils.isEmpty(str)) {
            return resultResponseErrorObj("param is error");
        }
        Map map = new Gson().fromJson(new String(Base64.getDecoder().decode(str)), Map.class);
        String WXUserId = (String) map.get("object");
        String type = (String) map.get("type");
        if (StringUtils.isEmpty(WXUserId)) {
            return resultResponseErrorObj("token is error");
        }
        if (StringUtils.isEmpty(type)) {
            return resultResponseErrorObj("type is error");
        }
        synchronized (this) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //检测到期时间
                    User user = tokenList.get(WXUserId);
                    if (user == null) {//未找到用户  非法

                        return;
                    }

                    if ("2".equals(type)) {// 有消息 待处理
                        long expireTime = user.getExpirTime().getTime();
                        if (new Date().getTime() > expireTime) {//检测充值码到时时间,到时间就移除
                            LogUtils.info("用户" + user.getUserId() + "充值码到期。");
                            removeToken(WXUserId);
                            return;
                        }
                        MsgSync(WXUserId);
                    }

                    if ("-1".equals(type)) {//下线,异步通知
                        //数据保存Bean中
                        UserExample queryExample = new UserExample();
                        queryExample.createCriteria().andIdEqualTo(user.getId());
                        List<User> queryUserList = userMapper.selectByExample(queryExample);
                        user = queryUserList.get(0);
                        user.setOnlineStatus(2);
                        userMapper.updateByExample(user, queryExample);
                        //前端通知
                        wechatWebSocket.sendMessageToUser(user.getFromUserName(), new TextMessage("wechatLoginSuccess"));
                    }
                }
            });
        }
        return resultResponseSuccessObj("success");
    }

    /**
     * 消息同步
     */
    private void MsgSync(String token) {
        String WeSyncMessage = DllInterface.instance.WXSyncMessage(Integer.parseInt(token));
        if (TextUtils.isEmpty(WeSyncMessage)) {
            return;
        }
        JSONArray jsonArray = new JSONArray(WeSyncMessage);
        List<DataInfoBean> listTypeToken = new Gson().fromJson(WeSyncMessage, new TypeToken<List<DataInfoBean>>() {
        }.getType());
        User user = tokenList.get(token);
        for (int i = 0; i < listTypeToken.size(); i++) {
            DataInfoBean dataInfoBean = listTypeToken.get(i);
            //群红包
            if (dataInfoBean.sub_type == 49 && dataInfoBean.from_user.contains("@chatroom")) {
                if (user.getPickDelay() == 1) {
                    try {
                        Thread.sleep(user.getPickDelayTime() * 1000L);
                    } catch (InterruptedException e) {
                    }
                }

                if (user.getPickType() == 1) {//所有群
                    ReceiveRedPacket(jsonArray.get(i).toString(), token, dataInfoBean.from_user, true);
                } else if (user.getPickType() == 2) {//指定群抢
                    List<String> pickGroupList = user.getPickGroupList();
                    for (String chatRoomId : pickGroupList) {
                        if (chatRoomId.equals(dataInfoBean.from_user)) {
                            ReceiveRedPacket(jsonArray.get(i).toString(), token, dataInfoBean.from_user, true);
                        }
                    }
                }
            }
            //个人红包或者转账
            if (dataInfoBean.sub_type == 49 && !dataInfoBean.from_user.contains("@chatroom")) {
                if (user.getAutoPickPersonal() == 1 && dataInfoBean.description.contains("[红包]")) {
                    ReceiveRedPacket(jsonArray.get(i).toString(), token, dataInfoBean.from_user, false);
                } else if (user.getAutoReceiveTransfer() == 1 && dataInfoBean.description.contains("[转账]")) {
                    pickTransfer(jsonArray.get(i).toString(), token);
                }
            }
            //文字消息
            if (dataInfoBean.sub_type == 1) {
                //群里的消息
                if (dataInfoBean.to_user != null && dataInfoBean.to_user.contains("@chatroom")) {
                    if ("开启".equals(dataInfoBean.content)) {
                        openGroupPick(token, dataInfoBean.to_user);
                    } else if ("关闭".equals(dataInfoBean.content)) {
                        closeGroupPick(token, dataInfoBean.to_user);
                    }
                    //自己给自己发的消息
                } else if (dataInfoBean.to_user != null && dataInfoBean.to_user.equals(dataInfoBean.from_user)) {
                    if ("000".equals(dataInfoBean.content)) {
                        operationStatus(token, user.getWechatId());
                    } else if ("111".equals(dataInfoBean.content)) {
                        operationList(token, user.getWechatId());
                    } else if (dataInfoBean.content != null && (dataInfoBean.content.startsWith("10") || dataInfoBean.content.startsWith("延时"))) {
                        operationSet(dataInfoBean.content, token, user.getWechatId());
                    }
                }
            }
        }
    }

    /**
     * 初始化通知
     */
    public void initNotification(String token, String chatRoom) {
        User user = tokenList.get(token);
        sendTextMsg(user,
                "初始化完成,当前状态:\n" +
                        "所有群抢包:" + (user.getPickType() == 1 ? "开" : "关") + "\n" +
                        "指定群抢包:" + (user.getPickType() == 2 ? "开" : "关") + "\n" +
                        "自动抢个人私聊包:" + (user.getAutoPickPersonal() == 1 ? "开" : "关") + "\n" +
                        "自动接收个人转账:" + (user.getAutoReceiveTransfer() == 1 ? "开" : "关") + "\n" +
                        "抢包延时:" + (user.getPickDelay() == 1 ? user.getPickDelayTime() + "秒" : "关") + "\n" +
                        "000 查看当前状态" + "\n" +
                        "111 查看指令"
                , chatRoom, false);
    }

    /**
     * 操作状态
     */
    private void operationStatus(String token, String chatRoom) {
        User user = tokenList.get(token);
        sendTextMsg(user,
                "当前状态:\n" +
                        "所有群抢包:" + (user.getPickType() == 1 ? "开" : "关") + "\n" +
                        "指定群抢包:" + (user.getPickType() == 2 ? "开" : "关") + "\n" +
                        "自动抢个人私聊包:" + (user.getAutoPickPersonal() == 1 ? "开" : "关") + "\n" +
                        "自动接收个人转账:" + (user.getAutoReceiveTransfer() == 1 ? "开" : "关") + "\n" +
                        "抢包延时:" + (user.getPickDelay() == 1 ? user.getPickDelayTime() + "秒" : "关")
                , chatRoom, false);
    }

    /**
     * 操作指令列表
     */
    private void operationList(String token, String chatRoom) {
        User user = tokenList.get(token);
        sendTextMsg(user,
                "1001:开启所有群\n" +
                        "1002:开启指定群\n" +
                        "1003:开启自动抢私聊包\n" +
                        "1004:关闭自动抢私聊包\n" +
                        "1005:开启自动接收个人转账\n" +
                        "1006:关闭自动接收个人转账\n" +
                        "延时X秒:设置延时秒抢\n" +
                        "000:查看当前状态"
                , chatRoom, false);
    }

    /**
     * 设置指令
     */
    private void operationSet(String command, String token, String chatRoom) {
        User user = tokenList.get(token);
        String tipsString = "";
        if (command.equals("1001")) {
            user.setPickType(1);
            tipsString = "开启所有群抢成功";
        } else if (command.equals("1002")) {
            user.setPickType(2);
            tipsString = "开启指定群抢成功";
        } else if (command.equals("1003")) {
            user.setAutoPickPersonal(1);
            tipsString = "开启自动抢私聊包成功";
        } else if (command.equals("1004")) {
            user.setAutoPickPersonal(0);
            tipsString = "关闭自动抢私聊包成功";
        } else if (command.equals("1005")) {
            user.setAutoReceiveTransfer(1);
            tipsString = "开启自动接收个人转账成功";
        } else if (command.equals("1006")) {
            user.setAutoReceiveTransfer(0);
            tipsString = "关闭自动接收个人转账成功";
        } else if (command.startsWith("延时")) {
            user.setPickDelay(1);
            user.setPickDelayTime(Integer.parseInt(command.replaceAll("延时", "").replaceAll("秒", "")));
            tipsString = "设置延时抢包成功";
        }
        sendTextMsg(user, tipsString, chatRoom, true);
    }

    /**
     * 开启指定群抢
     */
    private void openGroupPick(String token, String chatRoom) {
        User user = tokenList.get(token);
        LogUtils.info("开启指定群抢" + user.getFromUserName());
        if (user.getPickType() == 1) { //所有群抢
            sendTextMsg(user, "已开启所有群抢，开启失败", chatRoom, false);
            return;
        }
        user.addPickGroupList(chatRoom);
        sendTextMsg(user, "开启成功", chatRoom, true);
    }

    /**
     * 关闭指定群抢
     */
    private void closeGroupPick(String token, String chatRoom) {
        User user = tokenList.get(token);
        LogUtils.info("关闭指定群抢:" + user.getFromUserName());
        if (user.getPickType() == 1) { //所有群抢
            sendTextMsg(user, "已开启所有群抢，关闭失败", chatRoom, false);
            return;
        }
        user.removePickGroupList(chatRoom);
        sendTextMsg(user, "关闭成功", chatRoom, true);
    }

    /**
     * 发送文字消息 同步数据库
     */
    private void sendTextMsg(User user, String msg, String chatRoom, boolean isSaveDB) {
        LogUtils.info(user.getFromUserName() + "=发送文字信息:" + msg);
        //同步数据库
        if (isSaveDB) {
            UserExample userExample = new UserExample();
            UserExample.Criteria criteria = userExample.createCriteria();
            criteria.andIdEqualTo(user.getId());
            userMapper.updateByExample(user, userExample);
        }
        DllInterface.instance.WXSendMsg(Integer.parseInt(user.getToken()), chatRoom, msg);
    }

    /**
     * 接受红包数据
     */
    private void ReceiveRedPacket(String json, String token, String chatRoom, boolean isGroup) {
        LogUtils.info("接受红包数据");
        String WeReceiveRedPacket = DllInterface.instance.WXReceiveRedPacket(Integer.parseInt(token), json);
        ReceiveRedPacketBean receiveRedPacketBean = new Gson().fromJson(WeReceiveRedPacket, ReceiveRedPacketBean.class);
        redPick(json, token, receiveRedPacketBean.key, chatRoom, isGroup);
    }

    /**
     * 抢红包
     */
    private void redPick(String json, String token, String key, String chatRoom, boolean isGroup) {
        LogUtils.info("抢红包");
        String WeOpenRedPacket = DllInterface.instance.WXOpenRedPacket(Integer.parseInt(token), json, key);
        RedPickBean redPickBean = new Gson().fromJson(WeOpenRedPacket, RedPickBean.class);
        //没有抢成功
        if (redPickBean.getExternal() == null || redPickBean.getExternal().record == null || redPickBean.getExternal().record.size() == 0) {
            return;
        }
        if (!isGroup) {
            return;
        }
        User user = tokenList.get(token);
        for (RedPickBean.External.Record record : redPickBean.getExternal().record) {
            if (record.userName.equals(user.getWechatId())) {
                RedPacket redPacket = new RedPacket();
                redPacket.wechat_id = user.getWechatId();
                redPacket.user_id = user.getId();
                redPacket.group_id = chatRoom;
                chatRoomName(user, chatRoom, redPacket);//群聊名称恢复
                redPacket.packet_date = new Date();
                redPacket.money = record.receiveAmount;
                redPacketMapper.insertRedPacket(redPacket);
                break;
            }
        }
    }

    /**
     * 群聊名称恢复
     */
    private void chatRoomName(User user, String chatRoom, RedPacket redPacket) {
        redPacket.group_name = "未命名群聊";
        //从redis中取值
        boolean isNull = false;
        List<SyncContactBean> syncContactBeenList = redisUtil.getObject(user.getWechatId());
        for (int i = 0; i < syncContactBeenList.size(); i++) {
            if (syncContactBeenList.get(i).user_name.equals(chatRoom)) {
                if (!StringUtils.isEmpty(syncContactBeenList.get(i).nick_name)) {
                    redPacket.group_name = syncContactBeenList.get(i).nick_name;
                }
                isNull = true;
                break;
            }
        }
        //没取到就同步通讯录
        if (!isNull) {
            List<SyncContactBean> chainList = new ArrayList();
            wechatServeice.syncContact(user, chainList);
            syncContactBeenList.addAll(chainList);
            redisUtil.set(user.getWechatId(), syncContactBeenList);
            //还没找到就默认 未命名群聊
            for (int i = 0; i < syncContactBeenList.size(); i++) {
                if (syncContactBeenList.get(i).user_name.equals(chatRoom)) {
                    if (!StringUtils.isEmpty(syncContactBeenList.get(i).nick_name)) {
                        redPacket.group_name = syncContactBeenList.get(i).nick_name;
                    }
                    break;
                }
            }
        }
    }

    /**
     * 收转账
     */
    private void pickTransfer(String json, String token) {
        DllInterface.instance.WXTransferOperation(Integer.parseInt(token), json);
    }

    //添加轮询user
    public void addToken(User user) {
        synchronized (this) {
            tokenList.put(user.getToken(), user);
        }
    }

    public void removeToken(String token) {
        synchronized (this) {
            tokenList.remove(token);
        }
    }

}
