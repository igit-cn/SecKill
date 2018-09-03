package com.cloudSeckill.net.http.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public class SignUtils {

    // 暂未使用appKey，先用一个默认的，后续如果对接第三方会用到
    private static String appKey = "!W7iwls@B8q2RKz&CJipZNY9n9Me0H@q";

    /**
     * 获取签名
     */
    public static String genSign(String url, Map<String, String> request){
        url = url.replaceAll("https://", "").replaceAll("http://", "");
        try {
            return genSign(url, request, appKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String genSign(String pathUrl, Map<String, String> params, String appKey) throws NoSuchAlgorithmException {
        String str = concatParams(params);
        str = pathUrl + "?" + str + "&" + appKey;
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byte2hex(md.digest(str.getBytes()));
    }
    
    /**
     * 对请求参数作拼接并排序
     */
    private static String concatParams(Map<String, String> urlParam) {
        Object[] key_arr = urlParam.keySet().toArray();
        Arrays.sort(key_arr);
        String str = "";
        for (Object key : key_arr) {
            String val = urlParam.get(key);
            str += "&" + key + "=" + val;
        }
        return str.replaceFirst("&", "");
    }

    private static String byte2hex(byte[] b) {
        StringBuffer buf = new StringBuffer();
        int i;
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) i += 256;
            if (i < 16) buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

}
