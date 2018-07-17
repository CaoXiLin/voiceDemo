package demo.cxl.com.mylibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceFind;
import com.accloud.service.ACException;

import java.util.List;

import util.Const;
import util.StateCallBack;
import util.Utils;

/**
 * Created by cxl onGatewayDataBack 2018/5/8.
 */

public class PFKMain {
    /**
     * 实例对象
     */
    private static PFKMain instance;

    private static final String TAG= "aaaaa";
    private StateCallBack stateCallBack;
    private  String number;
    private  Context context;


    public static PFKMain getInstance() {
        if (instance == null) {
            instance = new PFKMain();
        }
        return instance;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:

                    Log.i("cxl",Thread.currentThread().getName()+"   mHandler -------");
                    stateCallBack.onSuccess(Const.SUCCESS,msg.obj.toString());
                break;
            }
        }
    } ;
    public void  findDevice(final Context context, final String  number, final StateCallBack stateCallBack){
        this.context = context;
        this.number = number;
        this.stateCallBack = stateCallBack;
        AC.findLocalDevice(AC.FIND_DEVICE_DEFAULT_TIMEOUT, new PayloadCallback<List<ACDeviceFind>>() {
            @Override
            public void success(List<ACDeviceFind> acDeviceFinds) {
                boolean isnumber = false;
                String ip = "";
                if (acDeviceFinds != null && acDeviceFinds.size() != 0) {
                    for (int i = 0;i<acDeviceFinds.size();i++){
                        if(acDeviceFinds.get(i).getPhysicalDeviceId().equals(number)){
                             ip = acDeviceFinds.get(i).getIp();
                            String physicalDeviceId = acDeviceFinds.get(i).getPhysicalDeviceId();
                            Utils.DownLoadLAN(ip,context,stateCallBack);

                            isnumber = true;
                        }
                    }

                    if(!isnumber){
                        if(stateCallBack!=null){
                            stateCallBack.failure(Const.ERROR);
                        }
                    }else {
                        Message message = new Message();
                        message.what = 1000;
                        message.obj = ip;
                        mHandler.sendMessage(message);
                    }
                }else {
                    if (stateCallBack != null) {
                        stateCallBack.failure(Const.ERROR);
                    }
                }
            }
            @Override
            public void error(ACException e) {
                if (stateCallBack != null) {
                    stateCallBack.failure(Const.ERROR);
                }
            }
        });
    }

}
