package com.proxy.utils;

import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.NullEntity;
import com.proxy.entity.ProxyInfoEntity;
import com.proxy.entity.ResultEntity;
import com.proxy.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckUtils {

    @Autowired
    TokenService tokenService;

    /**
     * 校验授权
     */
    public String checkPermission(String token) {
        ResultEntity resultEntity = new ResultEntity();
        ProxyInfoEntity proxyInfoEntity = tokenService.checkTokenReturnProxy(0, null, token);
//        if (proxyInfoEntity == null) {
//            resultEntity.data = new NullEntity();
//            resultEntity.code = ResponseCode.TOKEN_TIMEOUT;
//            resultEntity.msg = "登录超时,请重新登录";
//            return new Gson().toJson(resultEntity);
//        } else 
        
        if (proxyInfoEntity.id != 1) {
            resultEntity.data = new NullEntity();
            resultEntity.code = ResponseCode.PERMISSION_REFUSE;
            resultEntity.msg = "权限拒绝,无操作授权";
            return new Gson().toJson(resultEntity);
        }
        return null;
    }


    /**
     * 校验授权并获取proxy数据
     */
    public ProxyInfoEntity checkTokenReturnId(String token){
        return tokenService.checkTokenReturnProxy(0, null, token);
    }
    
    /**
     * 获取token查找到的代理商信息
     */
    public ProxyInfoEntity checkToken(String token){
        return tokenService.checkTokenReturnProxy(0, null, token);
    }

    /**
     * 检验数据非空
     */
    public String checkParamsIsEmpty(String... params) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.data = new NullEntity();
        resultEntity.code = ResponseCode.DATA_ERROR;
        resultEntity.msg = "参数为空或参数取值范围错误";
        if (params == null) {
            return new Gson().toJson(resultEntity);
        }
        for (String param : params) {
            if (StringUtils.isEmpty(param)) {
                return new Gson().toJson(resultEntity);
            }
        }
        return null;
    }
    
    public String getTokenTimeOutStr(){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.data = new NullEntity();
        resultEntity.code = ResponseCode.TOKEN_TIMEOUT;
        resultEntity.msg = "登录超时,请重新登录";
        return new Gson().toJson(resultEntity);
    }
}
