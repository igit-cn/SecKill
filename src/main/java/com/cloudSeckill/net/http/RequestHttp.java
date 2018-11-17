package com.cloudSeckill.net.http;

import com.cloudSeckill.net.http.callback.HttpCallBack;
import com.cloudSeckill.net.socket.socket.SocketClient;
import com.cloudSeckill.utils.LogUtils;
import com.cloudSeckill.utils.TextUtils;
import com.google.gson.JsonObject;
import com.proxy.utils.StringUtils;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;

public class RequestHttp {

    private String urlPath;
    private HashMap<String, String> paramMap = new HashMap();

    private MultipartBuilder multipartBuilder;//参数

    public RequestHttp setUrl(String urlPath) {
        this.urlPath = urlPath;
        return this;
    }

    public void execute(final HttpCallBack httpCallBack) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        Request.Builder builder = new Request.Builder();
        builder.url(urlPath);
        builder.post(multipartBuilder.build());
        try {
            Response execute = new OkHttpClient().newCall(builder.build()).execute();
            if (execute.code() != 200) {
                if (httpCallBack != null) {
                    httpCallBack.onResponseFail(execute.request());
                }
            } else {
                byte[] byteData = execute.body().bytes();
                if (httpCallBack != null) {
                    long time = System.currentTimeMillis() - start;
                    if (time > 1000) {
                        System.out.println("接口 " + paramMap.get("method") + "  耗时 : " + time / 1000L);
                    }
                    httpCallBack.onResponseSuccess(new String(byteData, "UTF-8"), byteData, urlPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeJsonPost(final HttpCallBack httpCallBack) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        Request.Builder builder = new Request.Builder();
        builder.url(urlPath);
        builder.post(RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), Base64.getEncoder().encodeToString(getJsonPost().getBytes()).trim().replace("\n", "")));
        try {
            Response execute = new OkHttpClient().newCall(builder.build()).execute();
            if (execute.code() != 200) {
                if (httpCallBack != null) {
                    httpCallBack.onResponseFail(execute.request());
                }
            } else {
                byte[] byteData = execute.body().bytes();
                execute.body().close();
                if (httpCallBack != null) {
                    long time = System.currentTimeMillis() - start;
                    if (time > 1000) {
                        System.out.println("接口 " + urlPath + ":" + paramMap.get("method") + "  耗时 : " + time / 1000L);
                    }
                    httpCallBack.onResponseSuccess(new String(byteData, "UTF-8"), byteData, urlPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeRelJsonPost(final HttpCallBack httpCallBack) {
        long start = System.currentTimeMillis();
        Request.Builder builder = new Request.Builder();
        builder.url(urlPath);
        String jsonPost = getJsonPost();
        LogUtils.info(urlPath + "请求参数：" + jsonPost);
        builder.post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonPost));
        try {
            Response execute = new OkHttpClient().newCall(builder.build()).execute();
            if (execute.code() != 200) {
                if (httpCallBack != null) {
                    httpCallBack.onResponseFail(execute.request());
                }
            } else {
                byte[] byteData = execute.body().bytes();
                execute.body().close();
                if (httpCallBack != null) {
                    long time = System.currentTimeMillis() - start;
                    if (time > 1000) {
                        System.out.println("接口 " + urlPath + ":" + paramMap.get("method") + "  耗时 : " + time / 1000L);
                    }
                    httpCallBack.onResponseSuccess(new String(byteData, "UTF-8"), byteData, urlPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeJsonSocket(final HttpCallBack httpCallBack) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        try {
            URI uri = new URI(urlPath);
            SocketClient socketClient = new SocketClient(uri.getHost(), uri.getPort());
            for (String key : paramMap.keySet()) {
                socketClient.putParam(key, Base64.getEncoder().encodeToString(paramMap.get(key).getBytes()));
            }
            socketClient.putParam("method", Base64.getEncoder().encodeToString(uri.getPath().substring(1, uri.getPath().length()).getBytes()));
            socketClient.sendData();
            String resopnseData = socketClient.receiveData();
            if (!TextUtils.isEmpty(resopnseData)) {
                if (httpCallBack != null) {
                    long time = System.currentTimeMillis() - start;
                    if (time > 1000) {
                        System.out.println("接口 " + urlPath + ":" + paramMap.get("method") + "  耗时 : " + time / 1000L);
                    }
                    httpCallBack.onResponseSuccess(resopnseData, resopnseData.getBytes(), urlPath);
                }
            } else {
                if (httpCallBack != null) {
                    httpCallBack.onResponseFail(null);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        Request.Builder builder = new Request.Builder();
//        builder.url(urlPath);
//        builder.post(RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), Base64.getEncoder().encodeToString(getJsonPost().getBytes()).trim().replace("\n", "")));
//        try {
//            Response execute = new OkHttpClient().newCall(builder.build()).execute();
//            if (execute.code() != 200) {
//                if (httpCallBack != null) {
//                    httpCallBack.onResponseFail(execute.request());
//                }
//            } else {
//                byte[] byteData = execute.body().bytes();
//                execute.body().close();
//                if (httpCallBack != null) {
//                    long time = System.currentTimeMillis() - start;
//                    if (time > 1000) {
//                        System.out.println("接口 " + urlPath + ":" + paramMap.get("method") + "  耗时 : " + time / 1000L);
//                    }
//                    httpCallBack.onResponseSuccess(new String(byteData, "UTF-8"), byteData, urlPath);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private String getJsonPost() {
        JsonObject jsonObject = new JsonObject();
        for (String key : paramMap.keySet()) {
            jsonObject.addProperty(key, paramMap.get(key));
        }
        return jsonObject.toString();
    }

    public RequestHttp putKeyValue(String key, String value) {
        if (StringUtils.isEmpty(value)) return this;
        paramMap.put(key, value);
        getMultipartBuilder().addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                RequestBody.create(null, Base64.getEncoder().encodeToString(value.getBytes()).trim().replace("\n", "")));
        return this;
    }

    public MultipartBuilder getMultipartBuilder() {
        if (multipartBuilder == null) {
            multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        }
        return multipartBuilder;
    }
}
