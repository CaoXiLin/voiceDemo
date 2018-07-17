package util;

import android.os.Environment;

/**
 * Created by cxl onGatewayDataBack 2018/5/8.
 */

public interface Const {
    String PackageFile = "pfk.tar";//打包文件;
    String DEVICE = "deviceconfig.json";//网关用的设备文件文件
    String connectionFails = "404";
    String socketIsNull = "500";
    String dataIsnull = "DeviceTableEntity is  null";
    String SUCCESS = "1";
    String ERROR = "-1";
    String HEAD = "\\$GPPFK";//协议头部
    int PORTNUM = 1238;//网关端口号
    String SDPATH = Environment.getExternalStorageDirectory()
            .getPath()+ "/pfkdownload/";



    String IODevice = "IODevice";
    String EnviromentDetection = "EnviromentDetection";
    String Panel = "Panel";


   String  Key_BackGroundMusic = "BackGroundMusic";
   String  Key_AirCondition = "AirCondition";               //
   String  Key_FloorHeat = "FloorHeat";                     //(地暖)
   String  Key_AdjustingLight = "AdjustingLight";           // (可调灯)
   String  Key_CommonLight = "CommonLight";                 //
   String  Key_Curtain = "Curtain";                         //
   String  Key_Lock = "Lock";                               //
   String  Key_ClotheShorse = "ClotheShorse";               //
   String  Key_IR_AIR = "IR_AIR";                           //
   String  Key_IR_TV = "IR_TV";                             //
   String  Key_IR_PROJECTOR = "IR_PROJECTOR";               //
   String  Key_IR_PLAYER = "IR_PLAYER";                     //
   String  Key_IR_POWERAMP = "IR_PLAYER";                   //
   String  Key_IR_SETBOX = "IR_PLAYER";                     //





}
