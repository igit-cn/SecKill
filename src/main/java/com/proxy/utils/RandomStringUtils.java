package com.proxy.utils;

import java.util.Random;

public class RandomStringUtils {

    public static String randomString(String firstStr, int length) {
        //48-57是数字  0 - 9 
        //65-90是大写  A - Z
        StringBuilder stringBuilder = new StringBuilder();

        if (firstStr == null) {//防止空指针
            firstStr = "";
        }
        
        if (firstStr.length() > 5) {
            firstStr = firstStr.substring(0, 5);
        }
        
        for (int i = 0; i < length - firstStr.length(); i++) {
            int randomInt = new Random().nextInt(36);
            if (randomInt < 10) {
                stringBuilder.append((char) (randomInt + 48));
            } else {
                stringBuilder.append((char) (randomInt + 65 - 10));
            }
        }
        return firstStr + stringBuilder.toString();
    }

    public static String randomStringForMac(String firstStr, int length) {
        //48-57是数字  0 - 9 
        //65-70是大写  A - F
        StringBuilder stringBuilder = new StringBuilder();

        if (firstStr == null) {//防止空指针
            firstStr = "";
        }

        if (firstStr.length() > 5) {
            firstStr = firstStr.substring(0, 5);
        }

        for (int i = 0; i < length - firstStr.length(); i++) {
            int randomInt = new Random().nextInt(16);
            if (randomInt < 10) {
                stringBuilder.append((char) (randomInt + 48));
            } else {
                stringBuilder.append((char) (randomInt + 65 - 10));
            }
        }
        return firstStr + stringBuilder.toString();
    }
    
    
    
}
