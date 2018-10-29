package com.test;

public class Test {

    public static void main(String[] args) {
//        String name = "hahahaipad";
//        String mac = Utils.getRandomMac();
//        String uuid = Utils.getRandomUUID();
//
//        int i = DllInterface.instance.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
//        String object = DllInterface.instance.WXInitialize(name, uuid, mac);
//        String qrcode = DllInterface.instance.WXGetQRCode(Integer.parseInt(object));
//        System.out.println(qrcode);
//        boolean scan = false;
//        do {
//            String wxCheckQRCode = DllInterface.instance.WXCheckQRCode(Integer.parseInt(object));
//            System.out.println(wxCheckQRCode);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } while (!scan);

        String name = "zhangsandeisdsdpad";
        DllInterface.instance1.WXSetNetworkVerifyInfo("117.50.51.222", 1819);
        String object = DllInterface.instance1.WXInitialize(name, Utils.getRandomUUID(), Utils.getRandomMac());
        String qrString = DllInterface.instance1.WXGetQRCode(Integer.parseInt(object));

        String qrStatusBefore = DllInterface.instance1.WXCheckQRCode(Integer.parseInt(object));
        System.out.println("检查二维码状态-----qrStatusBefore" + qrStatusBefore);
        DllInterface.instance1.WXRelease(Integer.parseInt(object));
        String qrStatusAfter = DllInterface.instance1.WXCheckQRCode(Integer.parseInt(object));
        System.out.println("检查二维码状态-----qrStatusAfter" + qrStatusAfter);

//        String object = DllInterface.instance1.WXInitialize(name, Utils.getRandomUUID(), Utils.getRandomMac());
//        String qrString = DllInterface.instance1.WXGetQRCode(Integer.parseInt(object));
//        String qrStatus1 = DllInterface.instance1.WXCheckQRCode(Integer.parseInt(object));
//        String qrStatus2 = DllInterface.instance1.WXCheckQRCode(Integer.parseInt(object));
//        int a = DllInterface.instance2.webapi(Utils.getRandomUUID(), Utils.getRandomMac(), name, "wxid_23sdae2323", "asdasdasdasdasdasd", "123");
//        System.out.println(a);
    }
}
