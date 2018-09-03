import com.cloudSeckill.net.socket.socket.SocketClient;
import com.proxy.utils.RandomStringUtils;
import com.proxy.utils.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Demo4 {

    private static Executor executorService  = Executors.newCachedThreadPool();
    
    public static void main (String[] a){
        
        final int[] requestCount = {0};
        final int[] responseCount = {0};
        final int[] errorCount = {0};

        new Thread(){
            @Override
            public void run() {
                while(true){
                    System.out.println("------------------------------------------");
                    System.out.println(requestCount[0]);
                    System.out.println(responseCount[0]);
                    System.out.println(errorCount[0]);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        SocketClient.init("47.92.166.84",8889);


        int i = 0;
        while(i < 10000) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    requestCount[0]++;
                    SocketClient socketClient = new SocketClient();
                    socketClient.putParam("method", "WXInitialize");
                    socketClient.putParam("device_type",
                            RandomStringUtils.randomStringForMac("", 2) + ":" +
                                    RandomStringUtils.randomStringForMac("", 2) + ":" +
                                    RandomStringUtils.randomStringForMac("", 2) + ":" +
                                    RandomStringUtils.randomStringForMac("", 2) + ":" +
                                    RandomStringUtils.randomStringForMac("", 2) + ":" +
                                    RandomStringUtils.randomStringForMac("", 2));
                    socketClient.putParam("device_name", "ppl" + RandomStringUtils.randomStringForMac("", 2) + "的iPad");
                    socketClient.putParam("device_uuid", RandomStringUtils.randomString("", 8) + "-6000-K820-28G6-" + RandomStringUtils.randomString("", 12));
                    socketClient.sendData();
                    String a = socketClient.receiveData();
                    if(StringUtils.isEmpty(a)){
                        errorCount[0]++;
                    } else {
                        responseCount[0]++;
                    }
                }
            });
            
            i++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
