package demo.cxl.com.voicedemo;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    is = true;
                    mEnd = System.currentTimeMillis();
                    Log.i(TAG, "handler ----------------------------------------------- ");
                    mPfkControlCallBack=null;
                    socketinfo = null;
                    socketinfo = new SocketInfo(MainActivity.this,mIp);

                    mPfkControlCallBack = new PFKControlCallBack(socketinfo,MainActivity.this);
                    isSuccessAndFailure();
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                connection();


                break;
            case R.id.bt_on:

                //开的控制指令
                if(mDeviceTableEntities!=null){

                    mPfkControlCallBack.Control("ON", mDeviceTableEntities.get(1).getDevicetype(),
                            mDeviceTableEntities.get(1).getDeviceid()
                            , mDeviceTableEntities.get(1).getDeviceattr(), MainActivity.this);
                }else {
                    Toast.makeText(MainActivity.this,"设备列表 null",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_close:
                //关的控制指令
                if(mDeviceTableEntities!=null){
                  mPfkControlCallBack.Control("OFF",mDeviceTableEntities.get(1).getDevicetype(),
                            mDeviceTableEntities.get(1).getDeviceid()
                            ,mDeviceTableEntities.get(1).getDeviceattr(),MainActivity.this);
                }else {
                    Toast.makeText(MainActivity.this,"设备列表 null    ",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_all:

                /*****************APP控制设备其他指令举例 类型为BackGroundMusic    比如执行上一曲    **************************/

                for (int i = 0;i<mDeviceTableEntities.size();i++){
                    if(mDeviceTableEntities.get(i).getDevicetype().equals("BackGroundMusic")){
                        PFKAllControlCallBack.getInstance().allControl(KEY_BackGroundMusic,"pre",mDeviceTableEntities.get(i).getDevicetype()
                        ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
                    }
                }

                break;
            case R.id.bt_all2:
                /*****************APP控制设备其他指令举例 类型为BackGroundMusic    比如执行下一曲    **************************/
                for (int i = 0;i<mDeviceTableEntities.size();i++){
                    if(mDeviceTableEntities.get(i).getDevicetype().equals("BackGroundMusic")){
                        PFKAllControlCallBack.getInstance().allControl(KEY_BackGroundMusic,"next",mDeviceTableEntities.get(i).getDevicetype()
                                ,mDeviceTableEntities.get(i).getDeviceid(), socketinfo,MainActivity.this);
                    }
                }
                break;
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
        PFKMain.getInstance().findDevice(this, number, new StateCallBack() {

            @Override
            public void callbackData(List<DeviceTableEntity> data) {
                Log.i("cxl",data.size()+   "数据");
                mDeviceTableEntities = data;
            }

            //成功
            @Override
            public void onSuccess(String Success, String ip) {
                mIp = ip;
                socketinfo = new SocketInfo(MainActivity.this,mIp);
                Log.i("cxl",Thread.currentThread().getName()+"   onSuccess   onSuccess ");
                Log.i(TAG, "onSuccess: "+"成功"+Success);



                mPfkControlCallBack = new PFKControlCallBack(socketinfo,MainActivity.this);
                isSuccessAndFailure();
            }

            @Override
            public void failure(String error) {
                Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
                Log.i(TAG, "failure: "+error);
            }
        });
    }


    @Override
    public void onSuccess(String success) {
        Log.i(TAG, "onSuccess: 》》》》》"+success);
    }

    @Override
    public void onError(String error) {


//            mHandler.sendEmptyMessageDelayed(1000,5000);
            is =false;
            mPfkControlCallBack=null;
            socketinfo = null;
            socketinfo = new SocketInfo(MainActivity.this,mIp);

            mPfkControlCallBack = new PFKControlCallBack(socketinfo,MainActivity.this);
            isSuccessAndFailure();

        Log.i(TAG, "onError: "+error);
        Log.i(TAG, "线程名字"+Thread.currentThread().getName());

    }

    private void isSuccessAndFailure() {
        if(socketinfo !=null){

            socketinfo.setOnSocektConnectionCallBack(new SocketInfo.OnSocektConnectionCallBack() {
                @Override
                public void onItemClickListener(String erro) {
//                    socketInfo = new SocketInfo(MainActivity.this,mIp);
                    // 等待 1分钟在重新连接

                    Log.i(TAG, "MainActivity>>>>>    失败 "+Thread.currentThread().getName()+"》》》》》"+erro);
                }

                @Override
                public void onGatewayDataBack(String msg) {
                    Log.i(TAG, "onGatewayDataBack: "+msg);
                    is = true;
                }

            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
