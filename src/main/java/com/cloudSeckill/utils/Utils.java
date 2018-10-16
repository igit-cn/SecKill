package com.cloudSeckill.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Random;
import java.util.UUID;

public class Utils {

    public static void main(String[] args) {
        String mac = getRandomMac();
        String uuid = getRandomUUID();
    }


    private static String SEPARATOR_OF_MAC = ":";

    /**
     * Generate a random MAC address for qemu/kvm
     * 52-54-00 used by qemu/kvm
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @return MAC address string
     */
    /**
     * 获取随机的mac
     */
    public static String getRandomMac() {
        Random random = new Random();
        String[] mac = {
                String.format("%02x", 0x52),
                String.format("%02x", 0x54),
                String.format("%02x", 0x00),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        String macStr = StringUtils.join(mac, SEPARATOR_OF_MAC);
        return macStr;
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
