package demo.cxl.com.mylibrary;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;

import org.json.JSONException;
import org.json.JSONObject;

import util.ControlCallBack;
import util.YohoPoolExecutor;

/**
 * Created by cxl onGatewayDataBack 2018/5/9.
 */

public class PFKControlCallBack {

//    public static PFKControlCallBack getInstance() {
//        if (instance == null) {
//            instance = new PFKControlCallBack();
//        }
//        return instance;
//    }
    private final ControlCallBack controlCallBack;
    private final SocketInfo socketInfo;

    public PFKControlCallBack(SocketInfo socketInfo, ControlCallBack controlCallBack){
        this.controlCallBack=controlCallBack;
        this.socketInfo=socketInfo;
    }


    /**
     *
     * @param value 根据控制指令传参 如果是开就 ON 如果关就OFF
     * @param dev_type  每个设备的类型
     * @param dev_id    每个设备的ID
     * @param dev_attr  每个设备的属性
     * @param controlCallBack
     * @return
     */
    public String Control(final String number , String value , String dev_type, String dev_id, JSONObject dev_attr , final ControlCallBack controlCallBack){
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject2 = new JSONObject();
        JSONObject jsonobject3 = new JSONObject();

        try {
            jsonobject2.put("power", value);
            jsonobject.put("dev_id", dev_id);
            jsonobject.put("dev_type", dev_type);
            jsonobject.put("dev_attr", jsonobject2);
            jsonobject3.put("switch", jsonobject.toString());
//          if(socketInfo!=null){
//
//              Utils.control(socketInfo,jsonobject.toString(),controlCallBack);
//          }else {
//              controlCallBack.onError(Const.socketIsNull);
//          }

            final ACDeviceMsg deviceMsg = new ACDeviceMsg(68, jsonobject3.toString().getBytes(),"open light");
            YohoPoolExecutor.getInstance().getExecutor().execute(new Runnable() {
                @Override
                public void run() {

                    AC.bindMgr().sendToDeviceWithOption("840",number, deviceMsg, AC.LOCAL_FIRST, new PayloadCallback<ACDeviceMsg>() {

                                @Override
                                public void success(ACDeviceMsg deviceMsg) {

                                }

                                @Override
                                public void error(ACException e) {
                                    if(controlCallBack!=null){
                                        controlCallBack.onError("404");
                                    }
                                }
                            });

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonobject.toString();
    }
}
