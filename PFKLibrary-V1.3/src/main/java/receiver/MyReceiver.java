package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import util.ControlCallBack;

public class  MyReceiver extends BroadcastReceiver {

    public  ControlCallBack controlCallBack = null;
    public static boolean isSuccessAndError = true;
    public MyReceiver(ControlCallBack controlCallBack) {
        this.controlCallBack =controlCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
//        Toast.makeText(context,"哈哈哈",Toast.LENGTH_LONG).show();
//        Log.i("cxl", "onReceive: -=-=-=-=-=-=-=-="+getClass());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(context,w.class);
//        context.startActivity(intent);

        String intentAction = intent.getAction();
        String pfk_data = intent.getStringExtra("pfk_data");
        if (intentAction.equals("pfk_data")) {

            if(controlCallBack!=null){
                isSuccessAndError = false;
                controlCallBack.onSuccess(pfk_data);
            }
        }
    }
}
