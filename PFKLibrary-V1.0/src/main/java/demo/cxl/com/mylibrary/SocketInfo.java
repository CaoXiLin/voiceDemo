package demo.cxl.com.mylibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import util.Const;


public class SocketInfo {

    private final String ip;
    public OutputStream out = null;
    public Handler handler = null;
    public Socket s = null;
    public Context context;

    public SocketInfo(final Context context,String ip) {
        this.context = context;
        this.ip=ip;
        this.handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //				Utility.ToastShowShort("数据:"+msg.obj.toString());
                if (msg.what == 0x123) {
                    Log.i("cxl", "0x123 接 到 的 数 据:" + msg.obj.toString());
                    //局域网情况下修改本地配置文件
                    try {
                        String info = msg.obj.toString().split(Const.HEAD)[1];
                        JSONObject jsonObjectGet = new JSONObject(info);//获取的值
                        JSONArray pfkFacilityArray = jsonObjectGet.optJSONArray("pfk_facility");//获取到的array;

                        if (pfkFacilityArray == null || pfkFacilityArray.length() == 0) {
                            return;

                        }
                    for (int i = 0 ;i<pfkFacilityArray.length();i++){
                        JSONObject jsonObject = pfkFacilityArray.optJSONObject(i);

                    }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Log.i("cxl",msg.obj.toString()+"链接失败");
                }
            }
        };
        //4.0之后访问网络不能在主程序中进行，要将代码放在线程中，不然会报错。
        thread.start();
    }
    public Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                s = new Socket(ip, Const.PORTNUM);
                new Thread(new ClientThread(s, handler,mOnSocektConnectionCallBack)).start();
                out = s.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                if(mOnSocektConnectionCallBack!=null){
                    mOnSocektConnectionCallBack.onItemClickListener(e.toString());
                }
                Message message = new Message();
                message.what = 1001;
                message.obj = "连接失败" + e.toString();
                handler.sendMessage(message);
            }
        }
    });

    private OnSocektConnectionCallBack mOnSocektConnectionCallBack;
    public void setOnSocektConnectionCallBack(OnSocektConnectionCallBack OnSocektConnectionCallBack){
        this.mOnSocektConnectionCallBack = OnSocektConnectionCallBack;
    }


    public interface OnSocektConnectionCallBack {
        void onItemClickListener(String erro);
    }
    public void close() {
        try {
            s.close();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if(mOnSocektConnectionCallBack!=null){
                mOnSocektConnectionCallBack.onItemClickListener(e.toString());
            }
            e.printStackTrace();
        }
    }

}
