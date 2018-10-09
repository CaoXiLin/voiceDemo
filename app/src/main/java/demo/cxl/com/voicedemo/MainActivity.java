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

import com.accloud.service.ACDeviceMsg;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import bean.DeviceTableEntity;
import bean.SceneTableEntity;
import demo.cxl.com.mylibrary.PFKAllControlCallBack;
import demo.cxl.com.mylibrary.PFKControlCallBack;
import demo.cxl.com.mylibrary.PFKMain;
import demo.cxl.com.mylibrary.SocketInfo;
import receiver.MyReceiver;
import service.SubscribeService;
import util.Const;
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
    private List<SceneTableEntity> mSceneTableEntities;
    private String mSceneid="";
    private Thread mMyThread;
    private Thread mMyThread2;
    private ThreadPoolExecutor executor;
    private ACDeviceMsg mDeviceMsg=null;


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
        Button bt_scene_opeen = (Button) findViewById(R.id.bt_scene_opeen);
        Button bt_scene_close = (Button) findViewById(R.id.bt_scene_close);
        Button bt_stop = (Button) findViewById(R.id.bt_stop);
        Button bt_quer = (Button) findViewById(R.id.bt_quer);
        mEt_number = (EditText) findViewById(R.id.et_number);

        bt_ok.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        bt_on.setOnClickListener(this);
        bt_all.setOnClickListener(this);
        bt_all2.setOnClickListener(this);
        bt_scene_opeen.setOnClickListener(this);
        bt_scene_close.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_quer.setOnClickListener(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (connecting) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    controlSceneOpen();
                    Log.i("thread", "open: " + "----------------------" + Thread.currentThread().getName());
                }
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                while (connecting) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    controlSceneClose();
                    Log.i("thread", "close: " + "----------------------" + Thread.currentThread().getName());
                }
            }
        };
        mMyThread = new Thread(runnable);
        mMyThread2 = new Thread(runnable2);
        Log.i("caoxilin", " mMyThread2 初始化时候的线程  状态--------: "+mMyThread2.isInterrupted());
        Log.i("caoxilin", "初始化线程的状态 mMyThread   : "+mMyThread.isInterrupted());

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



                sendOpen();


                break;
            case R.id.bt_close:
                //关的控制指令
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
            case R.id.bt_scene_opeen:
                controlSceneOpen();
//                timeOpen();
                break;
            case R.id.bt_scene_close:
                controlSceneClose();
//                timeClose();
                break;
            case R.id.bt_stop:
                connecting =false;

                mMyThread.interrupt();
                mMyThread2.interrupt();

                break;
            case R.id.bt_quer:
                Log.i("caoxilin", "查询  mMyThread2    close   --------: "+mMyThread2.isInterrupted());
                Log.i("caoxilin", "查询   mMyThread    stop    open: "+mMyThread.isInterrupted());
                break;
        }

    }

    private void timeClose() {
        //开的控制指令
        connecting = true;

        mMyThread2.start();
        Log.i("caoxilin", "timeClose: mMyThread2  "+mMyThread2.isInterrupted());
    }

    private void timeOpen() {
        connecting = true;
        mMyThread.start();
        Log.i("caoxilin", "timeOpen: mMyThread"+mMyThread.isInterrupted());

    }

    private void sendClose() {
        String devicetype="";
        String deviceid="";
        for (int i = 0;i<mDeviceTableEntities.size();i++){
            if(mDeviceTableEntities.get(i).getDevicetype().equals("CommonLight")){
                 devicetype = mDeviceTableEntities.get(i).getDevicetype();
                 deviceid = mDeviceTableEntities.get(i).getDeviceid();

            }
        }

        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),"power","OFF",devicetype
                ,deviceid, socketinfo,MainActivity.this);
    }

    private void sendOpen() {
        String devicetype="";
        String deviceid="";
        for (int i = 0;i<mDeviceTableEntities.size();i++){
            if(mDeviceTableEntities.get(i).getDevicetype().equals("CommonLight")){
                devicetype = mDeviceTableEntities.get(i).getDevicetype();
                deviceid = mDeviceTableEntities.get(i).getDeviceid();

            }
        }

        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(),"power","ON",devicetype
                ,deviceid, socketinfo,MainActivity.this);

    }

    private void controlSceneOpen() {
        if(mSceneTableEntities==null){
            return;
        }
        for (int i=0;i<mSceneTableEntities.size();i++){

            if(mSceneTableEntities.get(i).scenename.equals("全开")){
                mSceneid = mSceneTableEntities.get(i).sceneid;
            }
            if(mSceneTableEntities.get(i).scenename.equals("全关")){
                mSceneid = mSceneTableEntities.get(i).sceneid;
            }

        }
        //参数1 网关ID，参数2 Const.KEY_SCENE_ID， 参数3 场景列表中获取的ID，参数四 和五 可以给null
        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(), Const.KEY_SCENE_ID,mSceneid,Const.KEY_SCENE,"", null,MainActivity.this);

    }
    private void controlSceneClose(){
        if(mSceneTableEntities==null){
            return;
        }
        for (int i=0;i<mSceneTableEntities.size();i++){
            //小达识别到的 场景名字，对比场景列表中的场景名字  去找相应的场景ID  控制场景 网关需要场景ID
            if(mSceneTableEntities.get(i).scenename.equals("全关")){
                mSceneid = mSceneTableEntities.get(i).sceneid;
            }

        }
        //参数1 网关ID，参数2 Const.KEY_SCENE_ID， 参数3 场景列表中获取的ID，参数四 和五 可以给null
        PFKAllControlCallBack.getInstance().allControl(mEt_number.getText().toString(), Const.KEY_SCENE_ID,mSceneid, Const.KEY_SCENE,"", null,MainActivity.this);

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
            public void sceneData(List<SceneTableEntity> sceneData) {
                Log.i(TAG, "sceneData: "+sceneData.toString());
                mSceneTableEntities = sceneData;

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
