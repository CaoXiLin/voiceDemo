package util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.DeviceTableEntity;
import demo.cxl.com.mylibrary.SocketInfo;

/**
 * Created by cxl onGatewayDataBack 2018/5/9.
 */

public class Utils {
    public  static void control(final SocketInfo socketInfo, final String s, final ControlCallBack controlCallBack){

        if(s==null||s.isEmpty()||s.trim().equals("")){
            Log.i("cxl","socket              发送失败");
        }else {
            YohoPoolExecutor.getInstance().getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(socketInfo !=null&&socketInfo.out!=null){
                           socketInfo.out.write(("$GPPFK" + s + "GP_END").getBytes("utf-8"));
                            if(controlCallBack!=null){

                                controlCallBack.onSuccess(s);
                            }
                        }else {
                            if(controlCallBack!=null){

                                controlCallBack.onError(Const.socketIsNull);
                            }
                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                        if(socketInfo!=null&&socketInfo.out!=null){

                            socketInfo.close();
                        }
                        Log.i("cxl", "Utils >>>>>" + Thread.currentThread().getName());
                        if(controlCallBack!=null){

                            controlCallBack.onError(e.toString());
                        }
                    }
                }
            });
        }

    }

    /**
     * 局域网下载文件
     * ***/
    public  static void DownLoadLAN(final String readGETWAYIPaddress, final Context context, final StateCallBack stateCallBack) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                DownLoadUtility downLoadUtility = new DownLoadUtility();

                try {
                    //创建文件夹
                    createSDDir("");
                    //下载文件到文件夹
                    //"http://" + readGETWAYIPaddress + "/conf/deviceconfig.json"
                    downLoadUtility.downLoadFromUrl("http://"+readGETWAYIPaddress+"/conf/"+ Const.PackageFile, Const.PackageFile, Const.SDPATH);
                    //解压文件
                    TarUtility.dearchive(Const.SDPATH+"/"+Const.PackageFile);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                Log.i("cxl",Thread.currentThread().getName()+"   DownLoadLAN     Name");
                //保存文件
                String deviceString = readContent(Const.SDPATH+"pfk/"+Const.DEVICE);
                //保存设备文件到数据库
                loadDevicefile(readGETWAYIPaddress,context,deviceString,stateCallBack);
            }
        }).start();
    }

    public static File createSDDir(String dirName) throws IOException
    {
        File dir = new File(Const.SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {

        }
        return dir;
    }



    public static String readContent(String path) {

        String str = "";
        File file = new File(path);
        try {
            BufferedReader rd = new BufferedReader(new FileReader(file));

            String s = rd.readLine();
            while (null != s) {
                str += s;
                s = rd.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    //设备文件
    public static void loadDevicefile(String ip, Context activity, String deviceString, StateCallBack stateCallBack) {
        JSONObject jsonObject = null;
        List<DeviceTableEntity> deviceList =null;
        JSONObject attr = new JSONObject();
        try {
            deviceList = new ArrayList<>();
            jsonObject = new JSONObject(deviceString);
            JSONArray array = jsonObject.optJSONArray("pfk_facility");
            for (int i = 0; i < array.length(); i++) {
                DeviceTableEntity device = new DeviceTableEntity();
                JSONObject object = array.optJSONObject(i);
                device.deviceid = object.optString("dev_id");
                device.devicename = object.optString("dev_name");
                device.devicetype = object.optString("dev_type");
                device.deviceattr = object.optJSONObject("dev_attr");
                // 判断list中的数据如果已经用了就不添加
                boolean ishave = false;
                for (int j = 0; j < deviceList.size(); j++) {
                    if (deviceList.get(j).deviceid.equals(object.optString("dev_id"))) {
                        ishave = true;
                    }
                }
                if (!ishave) {
                    if(object.optString("dev_type").equals(Const.IODevice)
                            ||object.optString("dev_type").equals(Const.EnviromentDetection)
                            ||object.optString("dev_type").equals(Const.Panel)){
                    }else {
                        deviceList.add(device);
                    }

                }
            }

            if(stateCallBack!=null){
                Log.i("cxl",Thread.currentThread().getName()+"   loadDevicefile     Name");
                stateCallBack.callbackData(deviceList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
