package demo.cxl.com.mylibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACObject;
import com.accloud.service.ACPushTable;
import com.accloud.service.ACUserDevice;

import java.util.List;

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
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:

//                    Log.i("cxl",Thread.currentThread().getName()+"   mHandler -------");
//                    stateCallBack.onSuccess(Const.SUCCESS,msg.obj.toString());
                break;
                case 1001:
                    final String number = msg.obj.toString();
                    AC.pushMgr().connect(new VoidCallback() {
                        @Override
                        public void success() {
                            SubscribeData(number,stateCallBack);
                        }

                        @Override
                        public void error(ACException e) {
                            stateCallBack.failure("连接失败");
                        }
                    });
                    break;
            }
        }
    } ;


    private void SubscribeData(String number, final StateCallBack stateCallBack) {
        if(number.isEmpty()){
            if(stateCallBack!=null){
                stateCallBack.failure("订阅表失败");
            }
        }


        // 实例化ACPushTable对象
        ACPushTable table = new ACPushTable();
        // 设置订阅的表名
        table.setClassName("device_action_date");
        // 设置订阅的columns行
        //		table.setColumes(new String[] { "physicalDeviceId", "content", "time" });
        // 设置监听主键，此处对应添加数据集时的监控主键(监控主键必须是数据集主键的子集)
        ACObject primaryKey = new ACObject();
        // 订阅deviceId为1的数据变化
        primaryKey.put("physicalDeviceId", number);
        table.setPrimaryKey(primaryKey);

        //设置监听类型，如以下为只要发生创建、删除、替换、更新数据集的时候即会推送数据
        table.setOpType(ACPushTable.OPTYPE_CREATE | ACPushTable.OPTYPE_DELETE | ACPushTable.OPTYPE_REPLACE | ACPushTable.OPTYPE_UPDATE);

        AC.pushMgr().watch(table, new VoidCallback() {
            @Override
            public void success() {
                if(stateCallBack!=null){

                    stateCallBack.onSuccess("订阅连接成功","null");
                }
            }

            @Override
            public void error(ACException e) {
                if(stateCallBack!=null){

                    stateCallBack.failure("订阅连接失败");
                }
            }
        });



    }

    public void  findDevice(final Context context, final String  number, final StateCallBack stateCallBack){
        this.context = context;
        this.number = number;
        this.stateCallBack = stateCallBack;
//        AC.findLocalDevice(AC.FIND_DEVICE_DEFAULT_TIMEOUT, new PayloadCallback<List<ACDeviceFind>>() {
//            @Override
//            public void success(List<ACDeviceFind> acDeviceFinds) {
//                boolean isnumber = false;
//                String ip = "";
//                if (acDeviceFinds != null && acDeviceFinds.size() != 0) {
//                    for (int i = 0;i<acDeviceFinds.size();i++){
//                        if(acDeviceFinds.get(i).getPhysicalDeviceId().equals(number)){
//                             ip = acDeviceFinds.get(i).getIp();
//                            String physicalDeviceId = acDeviceFinds.get(i).getPhysicalDeviceId();
//                            Utils.DownLoadLAN(ip,context,stateCallBack);
//
//                            isnumber = true;
//                        }
//                    }
//
//                    if(!isnumber){
////                        if(stateCallBack!=null){
////                            stateCallBack.failure(Const.key_ERROR);
////                        }
//                        WanlistDevices(context,number,stateCallBack);
//                    }else {
//                        Message message = new Message();
//                        message.what = 1000;
//                        message.obj = ip;
//                        mHandler.sendMessage(message);
//                    }
//                }else {
////                    if (stateCallBack != null) {
////                        stateCallBack.failure(Const.key_gateway_0);
////                    }
//                    WanlistDevices(context,number,stateCallBack);
//                }
//            }
//            @Override
//            public void error(ACException e) {
////                if (stateCallBack != null) {
////                    stateCallBack.failure(Const.key_error_ablcloud);
////                }
//            }
//        });

        WanlistDevices(context,number,stateCallBack);
    }

    private void WanlistDevices(final Context context, final String number, final StateCallBack stateCallBack) {

        AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
            @Override
            public void success(List<ACUserDevice> list) {
                String ip ="Wan ip is null";
                if(list==null ||list.size()==0){
                    if(stateCallBack!=null){

                        stateCallBack.failure("Wan gateway is 0");
                    }
                }else {
                    boolean isnumber = false;
                    for (int i =0;i<list.size();i++){
                        ACUserDevice acUserDevice = list.get(i);
                        if(acUserDevice.getPhysicalDeviceId().equals(number)){
                            Utils.WanDownLoad(number,context,stateCallBack);
                            isnumber = true;
                        }
                    }


                    if(isnumber){
                        Message message = new Message();
                        message.obj = number;
                        message.what = 1001;
                        mHandler.sendMessage(message);
                    }else {

                        if(stateCallBack!=null){
                            stateCallBack.failure("Wan ACUserDevice found number is 0  ");
                        }
                    }

                }
            }

            @Override
            public void error(ACException e) {
                if(e.getErrorCode()==1992){
                    if (stateCallBack != null) {
                        stateCallBack.failure("1992");
                    }
                }else {
                    if (stateCallBack != null) {
                        stateCallBack.failure("Wan ablcloud_error");
                    }
                }

            }
        });
    }

}
