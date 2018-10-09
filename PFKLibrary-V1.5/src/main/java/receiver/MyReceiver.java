package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Const;
import util.ControlCallBack;

public class  MyReceiver extends BroadcastReceiver {

    public  ControlCallBack controlCallBack = null;

    public MyReceiver(ControlCallBack controlCallBack) {
        this.controlCallBack =controlCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        String pfk_data = intent.getStringExtra("pfk_data");
        JSONObject data = null;
        if (intentAction.equals("pfk_data")) {

            if(controlCallBack!=null){
                try {
                    data = new JSONObject(pfk_data);
                    JSONArray jsonArray = data.optJSONArray("pfk_facility");
                    String commandType="";
                    String scene_command_type="";
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        //解析pfk_facility 中的jsonArray如果有commandtype字段证明是场景控制成功
                        scene_command_type =  jsonObject.optString(Const.KEY_COMMANDTYPE);

                        JSONObject dev_attr_object = jsonObject.optJSONObject(Const.KEY_DEV_ATTR);
                        //解析dev_atte 中如果有commandtype字段value是xiaofei 证明是小飞单控成功
                        if(dev_attr_object!=null){

                            commandType = dev_attr_object.optString(Const.KEY_COMMANDTYPE);
                        }
                    }
                    if(commandType.equals("xiaofei")||scene_command_type.equals("xiaofei")){
                        controlCallBack.onSuccess("执行成功");
                        Log.i("cao", "onReceive: 广播>>监听到的数据>>>"+pfk_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
