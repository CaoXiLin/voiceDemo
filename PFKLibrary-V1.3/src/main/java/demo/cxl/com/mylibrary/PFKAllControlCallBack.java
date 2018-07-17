package demo.cxl.com.mylibrary;

import org.json.JSONException;
import org.json.JSONObject;

import util.Const;
import util.ControlCallBack;
import util.Utils;

/**
 * Created by cxl 2018/5/17.
 */

public class PFKAllControlCallBack {
    private static PFKAllControlCallBack instance;

    public static PFKAllControlCallBack getInstance() {
        if (instance == null) {
            instance = new PFKAllControlCallBack();
        }
        return instance;
    }


    public String allControl(String key, String value, String Devicetype, String Deviceid, final SocketInfo socketInfo, ControlCallBack controlCallBack) {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject2 = new JSONObject();
        try {
            jsonobject2.put(key, value);
            jsonobject.put("dev_id", Deviceid);
            jsonobject.put("dev_type", Devicetype);
            jsonobject.put("dev_attr", jsonobject2);
            if (socketInfo != null) {
                Utils.control(socketInfo, jsonobject.toString(), controlCallBack);
            } else {
                controlCallBack.onError(Const.socketIsNull);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonobject.toString();
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
}
