package test;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInterface extends Library {
    DllInterface instance = (DllInterface) Native.loadLibrary("D:\\tmp\\wechat\\WeChat", DllInterface.class);

    boolean WeRoot(String Ip, int Port);

    int WeInitialize(String name, String uuid, String mac);

    String WeGetQRCode(int object);

    String WeCheckQRCode(int object);
}
