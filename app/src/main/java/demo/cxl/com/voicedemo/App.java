package demo.cxl.com.voicedemo;

import android.app.Application;

import com.accloud.cloudservice.AC;

/**
 * Created by cxl onGatewayDataBack 2018/5/8.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AC.init(this, "njperfec1", 838);
    }
}
