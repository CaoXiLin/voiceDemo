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
import com.accloud.service.ACUserInfo;

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

    private static final String TAG= "cxl";
    private StateCallBack stateCallBack;
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
                            if(stateCallBack!=null){

                                stateCallBack.failure("连接失败");
                            }
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


        ACPushTable table = new ACPushTable();
        table.setClassName("device_action_date");
        ACObject primaryKey = new ACObject();
        primaryKey.put("physicalDeviceId", number);
        table.setPrimaryKey(primaryKey);
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

    public void  findDevice(final Context context, final String  number,String key, String  vlaue, final StateCallBack stateCallBack){
        this.stateCallBack = stateCallBack;
        AC.accountMgr().login(key, vlaue, new PayloadCallback<ACUserInfo>() {
            @Override
            public void success(ACUserInfo acUserInfo) {
                WanlistDevices(number,stateCallBack);
            }
            @Override
            public void error(ACException e) {
                if(stateCallBack!=null){
                    stateCallBack.failure("login failure");
                }
            }
        });
    }

    private void WanlistDevices(final String number, final StateCallBack stateCallBack) {

        AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
            @Override
            public void success(List<ACUserDevice> list) {
                if(list==null ||list.size()==0){
                    if(stateCallBack!=null){

                        stateCallBack.failure("Wan gateway is 0");
                    }
                }else {
                    boolean isnumber = false;
                    for (int i =0;i<list.size();i++){
                        ACUserDevice acUserDevice = list.get(i);
                        if(acUserDevice.getPhysicalDeviceId().equals(number)){
                            Utils.WanDownLoad(number,stateCallBack);
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
