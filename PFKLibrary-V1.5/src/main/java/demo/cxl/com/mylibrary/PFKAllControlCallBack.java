package demo.cxl.com.mylibrary;

import android.util.Log;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;

import org.json.JSONException;
import org.json.JSONObject;

import util.Const;
import util.ControlCallBack;
import util.YohoPoolExecutor;

/**
 * Created by cxl 2018/5/17.
 */

public class PFKAllControlCallBack {
    private static PFKAllControlCallBack instance;
    private JSONObject mJsonobject=null;
    private JSONObject mJsonobject2=null;
    private JSONObject mJsonobject3=null;
    private Runnable mRunnable =null;
    private ACDeviceMsg mDeviceMsg=null;
    public static PFKAllControlCallBack getInstance() {
        if (instance == null) {
            instance = new PFKAllControlCallBack();
        }
        return instance;
    }


    public String allControl(final String number , String key, String value, String Devicetype, String Deviceid, final SocketInfo socketInfo, final ControlCallBack controlCallBack) {
        if(mJsonobject==null){
            mJsonobject = new JSONObject();
        }
        if(mJsonobject2==null){
            mJsonobject2 = new JSONObject();
        }
        if(mJsonobject3==null){
            mJsonobject3 = new JSONObject();
        }
        if(Deviceid==null){
            Deviceid ="null";
        }
        try {
            if(Devicetype!=null){

                if(Devicetype.equals(Const.KEY_SCENE)){
                    mJsonobject2.put(key, value);
                    mJsonobject3.put("switch", mJsonobject2.toString());
                }else {

                    mJsonobject2.put(key, value);
                    mJsonobject.put("dev_id", Deviceid);
                    mJsonobject.put("dev_type", Devicetype);
                    mJsonobject.put("dev_attr", mJsonobject2);
                    mJsonobject3.put("switch", mJsonobject.toString());
                }
            }


            if(mDeviceMsg==null){
                mDeviceMsg = new ACDeviceMsg(68, mJsonobject3.toString().getBytes(),"open light");
            }else {
                mDeviceMsg.setCode(68);
                mDeviceMsg.setContent(mJsonobject3.toString().getBytes());
                mDeviceMsg.setDescription("open light");
            }
            if(mRunnable==null){
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.i("cxl",Thread.currentThread().getName()+mJsonobject3.toString());
                        AC.bindMgr().sendToDeviceWithOption("840",number, mDeviceMsg, AC.LOCAL_FIRST, new PayloadCallback<ACDeviceMsg>() {

                            @Override
                            public void success(ACDeviceMsg deviceMsg) {

                            }

                            @Override
                            public void error(ACException e) {
                                if(controlCallBack!=null){
                                    controlCallBack.onError("404");
                                }
                                switch (e.getErrorCode()){
                                    case 1992:
                                        if(controlCallBack!=null){
                                            controlCallBack.onError("先登录");
                                        }
                                        break;
                                    case 1993:
                                        if(controlCallBack!=null){
                                            controlCallBack.onError("请求超时");
                                        }
                                        break;
                                    case 1998:
                                        if(controlCallBack!=null){
                                            controlCallBack.onError("网络错误");
                                        }
                                        break;
                                }
                            }
                        });
                    }
                };
            }


            YohoPoolExecutor.getInstance().getExecutor().execute(mRunnable);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mJsonobject.toString();
    }



//    public String allControl(List<DeviceTableEntity> mDeviceTableEntities,SocketInfo socketInfo, ControlCallBack controlCallBack) {
//        String key ="";
//        String value = "";
//        JSONObject jsonobject=null;
//        JSONObject jsonobject2= null;
//        jsonobject = new JSONObject();
//        jsonobject2 = new JSONObject();
//        if(mDeviceTableEntities!=null&&mDeviceTableEntities.size()>0){
//            for (int i = 0;i<mDeviceTableEntities.size();i++){
//                if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_BackGroundMusic)){
////                    {"dev_id":"11_1","dev_type":"BackGroundMusic","dev_attr":{"playcontrol":"pre"}}
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_AirCondition)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_FloorHeat)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_AdjustingLight)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_CommonLight)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_Curtain)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_Lock)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_ClotheShorse)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_AIR)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_TV)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_PROJECTOR)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_PLAYER)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_POWERAMP)){
//
//                }else if(mDeviceTableEntities.get(i).getDevicetype().equals(Const.Key_IR_SETBOX)){
//
//                }
//            }
//        }else {
//            controlCallBack.onError(Const.dataIsnull);
//        }
//
//        try {
//            jsonobject2.put(key, value);
//            jsonobject.put("dev_id", Deviceid);
//            jsonobject.put("dev_type", Devicetype);
//            jsonobject.put("dev_attr", jsonobject2);
//            if (socketInfo != null) {
//                Utils.control(socketInfo, jsonobject.toString(), controlCallBack);
//            } else {
//                if(controlCallBack!=null){
//
//                    controlCallBack.onError(Const.socketIsNull);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonobject.toString();
//    }

    private void login(){

    }
}
