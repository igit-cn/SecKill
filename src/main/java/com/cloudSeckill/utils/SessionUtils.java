package com.cloudSeckill.utils;

import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.data.info.UserInfo;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.List;

public class SessionUtils {
    public static final String USER_INFO = "USER_INFO";//当前用户的信息
    public static final String KENG_ID = "KENG_ID";//用于记录当前session用户所选择的的坑Id
    public static final String SESSION_ID = "SESSION_ID";//用于识别webSocket
    
    public static final String MEMBER_LIST = "MEMBER_LIST";

    /**
     * 获取用户信息
     */
    public static List<User> getMemberList(HttpSession session){
        Object userInfo = session.getAttribute(MEMBER_LIST);
        if (userInfo == null) {
            return null;
        }
        return (List<User>) userInfo;
    }

    /**
     * 获取用户信息
     */
    public static UserInfo getUserInfo(HttpSession session){
        Object userInfo = session.getAttribute(USER_INFO);
        if (userInfo == null) {
            return null;
        }
        return (UserInfo) userInfo;
    }

    /**
     * 获取用户信息
     */
    public static UserInfo getUserInfoFromWebSocket(WebSocketSession session){
        Object userInfo = session.getAttributes().get(USER_INFO);
        if (userInfo == null) {
            return null;
        }
        return (UserInfo) userInfo;
    }

    public static String getSessionIdFromWebSocketSession(WebSocketSession session) {
        Object sessionId = session.getAttributes().get(SESSION_ID);
        if(sessionId == null){
            return null;
        }
        return (String) sessionId;
    }
    /**
     * 获取当前选择的坑id
     */
    public static int getCurrentSelectKengId(HttpSession session){
        Object userInfo = session.getAttribute(KENG_ID);
        if (userInfo == null) {
            return 0;
        }
        return Integer.parseInt( (String) userInfo) ;
    }
}
