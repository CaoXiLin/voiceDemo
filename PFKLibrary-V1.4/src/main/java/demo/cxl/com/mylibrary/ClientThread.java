package demo.cxl.com.mylibrary;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import util.Const;

public class ClientThread implements Runnable {
    private final SocketInfo.OnSocektConnectionCallBack onSocektConnectionCallBack;
    private Socket socket = null;
    private Handler handler = null;
    BufferedReader br = null;
    private static String curContent = "";

    public ClientThread(Socket s, Handler handler, SocketInfo.OnSocektConnectionCallBack onSocektConnectionCallBack) throws IOException {
        this.socket = s;
        this.handler = handler;
        this.onSocektConnectionCallBack=onSocektConnectionCallBack;
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
    @Override
    public void run() {
        try {
            String connet = null;
                while ((connet = br.readLine()) != null) {
                    Message message = new Message();
                    message.what = 0x123;
                    message.obj = connet;
                    curContent = connet;
                    handler.sendMessage(message);

                }
        } catch (IOException e) {
            e.printStackTrace();

            if(onSocektConnectionCallBack!=null){
                onSocektConnectionCallBack.onItemClickListener(Const.connectionFails);
            }

        }
    }


}
