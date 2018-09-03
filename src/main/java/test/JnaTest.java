package test;

import java.util.Random;
import java.util.UUID;

public class JnaTest {
    private static String name = "啦啦的ipad";
    private static String uuid = "002IEYVG-6000-K820-28G6-M80H64RWQSUQ";
    private static String mac = "00:22:5F:5F:03:27";
    private static String SEPARATOR_OF_MAC = ":";

    public static void main(String[] args) {
//        loadJNA();
    }

    private static void loadJNA() {
        boolean weRoot = DllInterface.instance.WeRoot("132.232.40.231", 5002);
        //450077216
        int object = DllInterface.instance.WeInitialize(name, new String(getRandomUUID()), new String(getRandomMac()));
        String QRString = DllInterface.instance.WeGetQRCode(object);
        String QRStatus = DllInterface.instance.WeCheckQRCode(450863728);
        System.out.println("object ：" + object);
    }

    public static byte[] getRandomMac() {
        Random random = new Random();
        String[] mac = {
                String.format("%02x", 0x52),
                String.format("%02x", 0x54),
                String.format("%02x", 0x00),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        String macStr = String.join(SEPARATOR_OF_MAC, mac);
        return macStr.getBytes();
    }

    public static byte[] getRandomUUID() {
        return UUID.randomUUID().toString().getBytes();
    }
}
