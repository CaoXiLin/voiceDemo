package demo.cxl.com.voicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Toast.makeText(context,"哈哈哈",Toast.LENGTH_LONG).show();
        Log.i("cxl", "onReceive: -=-=-=-=-=-=-=-="+getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context,w.class);
        context.startActivity(intent);
    }
}
