package demo.cxl.com.mylibrary;

import org.json.JSONException;
import org.json.JSONObject;

import util.Const;
import util.ControlCallBack;
import util.Utils;

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
    public String Control(String value ,String dev_type,String dev_id,JSONObject dev_attr ,ControlCallBack controlCallBack){
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject2 = new JSONObject();

        try {
            jsonobject2.put("power", value);
            jsonobject.put("dev_id", dev_id);
            jsonobject.put("dev_type", dev_type);
            jsonobject.put("dev_attr", jsonobject2);
          if(socketInfo!=null){

              Utils.control(socketInfo,jsonobject.toString(),controlCallBack);
          }else {
              controlCallBack.onError(Const.socketIsNull);
          }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonobject.toString();
    }
}
