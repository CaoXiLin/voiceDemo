package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACObject;
import com.accloud.service.ACPushMgr;
import com.accloud.service.ACPushReceive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cxl on 2017/12/8.
 * 监听外网消息 如果有消息就更新
 */

public class SubscribeService extends Service{
    private final IBinder mBinder = new LocalBinder();
    private ACPushMgr pushMgr;
    private Intent mIntent=null;

    @Nullable



    @Override
    public void onCreate() {
        super.onCreate();
        pushMgr = AC.pushMgr();
    }
    private SubscribeService.OnSocektConnectionCallBack mOnSocektConnectionCallBack;
    public void setOnSocektConnectionCallBack(SubscribeService.OnSocektConnectionCallBack OnSocektConnectionCallBack){
        this.mOnSocektConnectionCallBack = OnSocektConnectionCallBack;

    }
    public interface OnSocektConnectionCallBack {
        void onItemClickListener(String erro);
        void onGatewayDataBack(String msg);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getData();
        return START_REDELIVER_INTENT;
    }

    private void getData() {
//        接收已订阅的实时数据
        pushMgr.onReceive(new PayloadCallback<ACPushReceive>() {
            @Override
            public void success(ACPushReceive acPushReceive) {
                ACObject payload = acPushReceive.getPayload();
                String physicalDeviceId = payload.getString("physicalDeviceId");
                String content = payload.getString("content");
                String time = payload.getString("time");

                Message msg = new Message();
                msg.obj = content;
                msg.what = 2000;
                mHandler.sendMessage(msg);
            }

            @Override
            public void error(ACException e) {

            }
        });
    }

    private Handler mHandler = new Handler(){



        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2000:
                    String obj = (String) msg.obj;
                        received(obj);
                    break;
            }
        }
    };

    private void received(String obj) {
        try {
            JSONObject jsonObjectGet = new JSONObject(obj);//获取的值
            Log.i("cao", "received: "+obj);
            if(mIntent!=null){
                mIntent.putExtra("pfk_data",jsonObjectGet.toString());
            }else {
                mIntent =new Intent();
                mIntent.putExtra("pfk_data",jsonObjectGet.toString());
            }
            mIntent.setAction("pfk_data");
            sendBroadcast(mIntent);
             JSONArray pfkFacilityArray = jsonObjectGet.optJSONArray("pfk_facility");//获取到的array;
            if (pfkFacilityArray == null || pfkFacilityArray.length() == 0) {
                return;
            }

            if(mOnSocektConnectionCallBack!=null){
                mOnSocektConnectionCallBack.onGatewayDataBack(jsonObjectGet.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


        public class LocalBinder extends Binder {
        String stringToSend = "I'm the test String";
        public SubscribeService getService() {
            Log.i("TAG", "getService ---> " + SubscribeService.this);
            return SubscribeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



}
