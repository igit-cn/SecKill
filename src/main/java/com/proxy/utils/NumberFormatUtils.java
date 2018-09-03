package com.proxy.utils;

import java.text.SimpleDateFormat;

public class NumberFormatUtils {
    
    public static long dateFormat(String timeStr){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(timeStr).getTime();
        } catch (Exception e) {
            return -1;
        }
    }
     public static long dateFormatTomorrow(String timeStr){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(timeStr).getTime() + 1000L * 60 * 60 * 24;
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static int integerFormat(String integer){
        try {
            return Integer.parseInt(integer);
        } catch (Exception e){
            return -1;
        }
    }
}
