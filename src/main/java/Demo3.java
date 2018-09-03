import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Shuchu implements Runnable {
    public void run() {
        try {
            Socket s = new Socket("127.0.0.1", 10005);
            BufferedReader bu = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));


            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        String str = null;
                        while ((str = bu.readLine()) != null) {
                            bw.write(str);
                            bw.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            new Thread(){
                @Override
                public void run() {
                    String data = "";
                    String info;
                    try {
                        while ((info = br.readLine()) != null) {
                            data += info;
                        }
                        System.out.println(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            
            
//            while (true) {

//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class jieshou implements Runnable {
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(10005);
            Socket s = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String str = null;
            while (true) {
                str = br.readLine();
                System.out.println(str);
                bw.write("服务器接收到了,通知一下");
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Demo3 {
    public static void main(String args[]) throws Exception {
        new Thread(new jieshou()).start();
        Thread.sleep(500);
        new Thread(new Shuchu()).start();
    }
}