package com.cloudSeckill.net.http;

import com.cloudSeckill.net.http.callback.HttpCallBack;
import com.proxy.utils.StringUtils;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.IdentityHashMap;

public class RequestHttp{

	private String urlPath;
	private HashMap<String,String> paramMap = new HashMap();
	
	private MultipartBuilder multipartBuilder ;//参数
	
	public RequestHttp setUrl(String urlPath){
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
			if (execute.code() != 200){
				if (httpCallBack != null) {
					httpCallBack.onResponseFail(execute.request());
				}
			} else {
				byte[] byteData = execute.body().bytes();
				if (httpCallBack != null) {
					long time = System.currentTimeMillis() - start;
					if(time > 1000){
						System.out.println("接口 " + paramMap.get("method") +"  耗时 : " + time / 1000L);
					}
					httpCallBack.onResponseSuccess(new String(byteData,"UTF-8"), byteData, urlPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RequestHttp putKeyValue (String key, String value){
		if(StringUtils.isEmpty(value)) return this;
		paramMap.put(key,value);
		getMultipartBuilder().addPart(Headers.of("Content-Disposition","form-data; name=\"" + key + "\""),
				RequestBody.create(null, Base64.getEncoder().encodeToString(value.getBytes()).trim().replace("\n","")));
		return this;
	}
	
	public MultipartBuilder getMultipartBuilder(){
		if(multipartBuilder == null){
			multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
		}
		return multipartBuilder;
	}
}
