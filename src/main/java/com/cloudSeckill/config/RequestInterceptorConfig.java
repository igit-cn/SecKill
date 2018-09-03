package com.cloudSeckill.config;

import com.cloudSeckill.utils.JsonUtils;
import com.cloudSeckill.utils.LogUtils;
import com.cloudSeckill.utils.SessionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestInterceptorConfig extends HandlerInterceptorAdapter {
    private List<String> chainUrl = new ArrayList<>();

    public RequestInterceptorConfig() {
        chainUrl.add("/");
        chainUrl.add("/user/login");
        chainUrl.add("/user/register");
        chainUrl.add("/user/verifyCode");
        chainUrl.add("/user/sendEmailCode");
        chainUrl.add("/user/resetPassword");
        chainUrl.add("/user/logout");
        chainUrl.add("/error");
        chainUrl.add("/wc/sendData");
        chainUrl.add("/wc/pickDetail");
        chainUrl.add("/receive/notification");
        chainUrl.add("/admin");
        chainUrl.add("/mobile");
        chainUrl.add("/query");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
        if (handler instanceof HandlerMethod) {
            String requestURI = request.getRequestURI();
            LogUtils.info("请求地址：" + requestURI);
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (null != parameterMap && !parameterMap.isEmpty()) {
                LogUtils.info("请求参数：" + JsonUtils.writeValueAsString(parameterMap));
            }
            request.setCharacterEncoding("utf-8");
            Object attribute = request.getSession().getAttribute(SessionUtils.USER_INFO);
            if (attribute == null) {//没有登录
                //如果不是访问登录接口，跳转到登录界面
                //如果是访问登录，则放行
                if (!isReqInChainUrl(requestURI)) {
                    //如果是ajax请求响应头会有，x-requested-with
                    if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                        response.setHeader("sessionstatus", "timeout");//在响应头设置session状态
                    } else if (requestURI.startsWith("/proxy/") || requestURI.startsWith("/api/")){
                        flag = true;
                    } else {
                        response.sendRedirect("/index");
                        flag = false;
                    }
                }
                
                if(requestURI.startsWith("/wechat/cancelLooperQRCodeStatus")){
                    response.sendRedirect("/index");
                    flag = false;
                }
            }
        }
        return flag;
    }

    private boolean isReqInChainUrl(String reqUrl) {
        for (String s : chainUrl) {
            if (s.startsWith(reqUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
