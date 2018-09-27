package demo.cxl.com.voicedemo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bean.DeviceTableEntity;
import demo.cxl.com.mylibrary.PFKAllControlCallBack;
import demo.cxl.com.mylibrary.PFKControlCallBack;
import demo.cxl.com.mylibrary.PFKMain;
import demo.cxl.com.mylibrary.SocketInfo;
import receiver.MyReceiver;
import service.SubscribeService;
import util.ControlCallBack;
import util.StateCallBack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ControlCallBack {

    private TextView mTextView;
    public  SocketInfo socketinfo;
    private final static String TAG ="cxl";
    private String mIp = "";
    private List<DeviceTableEntity> mDeviceTableEntities ;
    private PFKControlCallBack mPfkControlCallBack;
    private EditText mEt_number;
    private String KEY_BackGroundMusic = "playcontrol";
    private Context mContext;
    private MyReceiver mReceiver=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;
        init();

    }

    private void init() {
        mTextView = (TextView) findViewById(R.id.tv);
        Button bt_ok = (Button) findViewById(R.id.bt_ok);
        Button bt_close = (Button) findViewById(R.id.bt_close);
        Button bt_on = (Button) findViewById(R.id.bt_on);
        Button bt_all = (Button) findViewById(R.id.bt_all);
        Button bt_all2 = (Button) findViewById(R.id.bt_all2);
        Button bt_time = (Button) findViewById(R.id.bt_time);
        mEt_number = (EditText) findViewById(R.id.et_number);

        bt_ok.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        bt_on.setOnClickListener(this);
        bt_all.setOnClickListener(this);
        bt_all2.setOnClickListener(this);

        bt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private long mEnd=0;
    private boolean is =true;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:

                    break;
                case 1001:
                    Toast.makeText(mContext,"失败-->",Toast.LENGTH_LONG).show();
                    break;
                case 1002:
                    Toast.makeText(mContext,mDeviceTableEntities.toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(mContext, SubscribeService.class);
                    startService(intent);

                    break;
            }
        }
    };

private boolean connecting =false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                connection();

                if(mReceiver ==null){

                    mReceiver = new MyReceiver(this);
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("pfk_data");
                    registerReceiver(mReceiver, filter);
                }

                break;
            case R.id.bt_on:

                //开的控制指令
                connecting = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (connecting) {
                            try {
                                Thread.sleep(2000);
                                    sendOpen();
                                Log.i("cc", "open: "+"----------------------"+Thread.currentThread().getName());

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                sendOpen();


                break;
            case R.id.bt_close:
                //关的控制指令
                connecting = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (connecting) {
                            try {
                                Thread.sleep(2000);
                                sendClose();
                                Log.i("cc", "close: "+"----------------------"+Thread.currentThread().getName());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                sendClose();
                break;
            case R.id.bt_all:

                /*****************APP控制设备其他指令举例 类型为BackGroundMusic    比如执行上一曲    **************************/

                for (int i = 0;i<mDeviceTableEntities.size();i++){
                    if(mDeviceTableEntities.get(i).getDevicetype().equals("BackGroundMusic")){
                        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),KEY_BackGroundMusic,"pre",mDeviceTableEntities.get(i).getDevicetype()
                        ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
                    }
                }

                break;
            case R.id.bt_all2:
                /*****************APP控制设备其他指令举例 类型为BackGroundMusic    比如执行下一曲    **************************/
                for (int i = 0;i<mDeviceTableEntities.size();i++){
                    if(mDeviceTableEntities.get(i).getDevicetype().equals("BackGroundMusic")){
                        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),KEY_BackGroundMusic,"next",mDeviceTableEntities.get(i).getDevicetype()
                                ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
                    }
                }

                connecting=false;
                break;
        }

    }

    private void sendClose() {
        for (int i = 0;i<mDeviceTableEntities.size();i++){
            if(mDeviceTableEntities.get(i).getDevicetype().equals("CommonLight")){
                PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),"power","OFF",mDeviceTableEntities.get(i).getDevicetype()
                        ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
            }
        }
    }

    private void sendOpen() {
        for (int i = 0;i<mDeviceTableEntities.size();i++){
            if(mDeviceTableEntities.get(i).getDevicetype().equals("CommonLight")){
                PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),"power","ON",mDeviceTableEntities.get(i).getDevicetype()
                        ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
            }
        }
    }

    private void connection() {


        String number = mEt_number.getText().toString();
        if(number.isEmpty()){
            Toast.makeText(MainActivity.this,"请你输入网关序列号",Toast.LENGTH_LONG).show();
            return;
        }

        /**
         * 去连接网关
         */
        PFKMain.getInstance().findDevice(this, number,"17538718726","1234", new StateCallBack() {

            //成功
            @Override
            public void onSuccess(String Success, String ip) {
                Log.i(TAG, "onSuccess: "+Success);

            }

            @Override
            public void callbackData(List<DeviceTableEntity> data) {
                Log.i("cxl",data.size()+   "数据");
                mDeviceTableEntities = data;
                mHandler.sendEmptyMessage(1002);

            }

            @Override
            public void failure(String error) {
//                Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
                Log.i(TAG, "failure: "+error);
                mHandler.sendEmptyMessage(1001);
            }
        });
    }


    @Override
    public void onSuccess(String success) {
        Log.i(TAG, "onSuccess: 》》》》》"+success);
    }

    @Override
    public void onError(String error) {
        Log.i(TAG, "onError: "+error);
        Log.i(TAG, "线程名字"+Thread.currentThread().getName());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
