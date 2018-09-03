package com.cloudSeckill.net.http.callback;

import com.google.gson.Gson;
import com.opslab.util.StringUtil;
import com.squareup.okhttp.Request;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallBack<T> {

    private final static int SUCCESS_CODE = 200;
    private HttpClientEntity httpClientEntity = new HttpClientEntity();
    private T entity;

    public abstract void onSuccess(HttpClientEntity httpClientEntity, T entity);

    public void onFailure(HttpClientEntity httpClientEntity){
        
    }

    public void onResponseSuccess(String result, byte[] byteData,String urlName) {
        if (StringUtil.isEmpty(result)) {
            httpClientEntity.msg = "数据异常";
            exceptionDispose();
            return;
        }
        httpClientEntity.json = result;
        
        try{
            //判断返回json是否可以解析,判断是否配置泛型
            if( ! StringUtils.isEmpty(httpClientEntity.json) && ! "null".equals(httpClientEntity.json) ){
                if(getType() != Object.class){
                    entity = getEntity(httpClientEntity.json);
                }
            }
            httpClientEntity.resultCode = SUCCESS_CODE;
        } catch (RuntimeException e){
            httpClientEntity.msg = "json解析数据异常";
            exceptionDispose();
            return;
        }

        if (httpClientEntity.resultCode != SUCCESS_CODE) {
            exceptionDispose();
            return;
        }
        
        onSuccess(httpClientEntity,entity);
    }

    public void onResponseFail(Request request) {
        httpClientEntity.msg = "网络异常";
        exceptionDispose();
    }

    private void exceptionDispose() {
        System.out.println("网络请求异常 : " + httpClientEntity.msg);
        onFailure(httpClientEntity);
    }
    
    public Type getType(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        return ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
    }

    public T getEntity(String json){
        return new Gson().fromJson(json, getType());
    }
}

