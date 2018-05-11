package util;

import android.os.Environment;

/**
 * Created by cxl on 2018/5/8.
 */

public interface Const {
    String PackageFile = "pfk.tar";//打包文件;
    String DEVICE = "deviceconfig.json";//网关用的设备文件文件
    String connectionFails = "404";
    String socketIsNull = "500";
    String SUCCESS = "1";
    String ERROR = "-1";
    String HEAD = "\\$GPPFK";//协议头部
    int PORTNUM = 1238;//网关端口号
    String SDPATH = Environment.getExternalStorageDirectory()
            .getPath()+ "/pfkdownload/";


    String IODevice = "IODevice";
    String EnviromentDetection = "EnviromentDetection";
    String Panel = "Panel";


}
