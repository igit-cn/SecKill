package com.cloudSeckill.net.http.utils;

import java.util.Random;

public class RandomStringUtils {
    
    public static String randomString(int length){
        //48-57是数字  0 - 9 
        //65-90是大写  A - Z
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomInt = new Random().nextInt(36);
            if(randomInt < 10){
                stringBuilder.append((char) (randomInt + 48));
            } else {
                stringBuilder.append((char)(randomInt + 65 - 10));
            }
        }
        return stringBuilder.toString();
    }
}
