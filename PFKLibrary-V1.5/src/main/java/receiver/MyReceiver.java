package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        if (intentAction.equals("pfk_data")) {

            if(controlCallBack!=null){
                controlCallBack.onSuccess(pfk_data);
            }
        }
    }
}
