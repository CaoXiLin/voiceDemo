package util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;

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

import static util.Const.SDPATH;

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
//    public  static void DownLoadLAN(final String readGETWAYIPaddress, final Context context, final StateCallBack stateCallBack) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//
//                DownLoadUtility downLoadUtility = new DownLoadUtility();
//
//                try {
//                    //创建文件夹
//                    createSDDir("");
//                    //下载文件到文件夹
//                    //"http://" + readGETWAYIPaddress + "/conf/deviceconfig.json"
//                    downLoadUtility.downLoadFromUrl("http://"+readGETWAYIPaddress+"/conf/"+ Const.PackageFile, Const.PackageFile, SDPATH);
//                    //解压文件
//                    TarUtility.dearchive(SDPATH+"/"+Const.PackageFile);
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//
//                Log.i("cxl",Thread.currentThread().getName()+"   DownLoadLAN     Name");
//                //保存文件
//                String deviceString = readContent(SDPATH+"pfk/"+Const.DEVICE);
//                //保存设备文件到数据库
//                loadDevicefile(readGETWAYIPaddress,context,deviceString,stateCallBack);
//            }
//        }).start();
//    }

    public static File createSDDir(String dirName) throws IOException
    {
        File dir = new File(SDPATH + dirName);
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
    // 广域网下载 2
    public static void WanDownLoad(final String ip, final Context context, final StateCallBack stateCallBack) {

        //		Log.i("INFO", "序列号:" + ip);
        com.accloud.service.ACFileInfo fileInfo = new com.accloud.service.ACFileInfo(ip, Const.PackageFile);
        // 上传文件时若ACFileInfo中isPublic为true，则expireTime参数无效；默认情况为false，如下24*60*60代表url链接有效时间，即1天
        AC.fileMgr().getDownloadUrl(fileInfo,  24*60*60, new PayloadCallback<String>() {

            @Override
            public void success(final String url) {
                // TODO Auto-generated method stub

                Log.i("cao", "url:"+url);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        DownLoadUtility downLoadUtility = new DownLoadUtility();
                        //						Log.i("INFO", "广域网下下载：" + downLoadUtility.downLoad(url));

                        try {
                            //创建文件夹
                            Utils.createSDDir("");
                            //下载文件到文件夹
                            downLoadUtility.downLoadFromUrl(url, Const.PackageFile, SDPATH);
                            //解压文件
                            TarUtility.dearchive(SDPATH+"/"+Const.PackageFile);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        Log.i("cxl",Thread.currentThread().getName()+"   DownLoadLAN     Name");
                        //保存文件
                        String deviceString = readContent(SDPATH+"pfk/"+Const.DEVICE);
                        //保存设备文件到数据库
                        loadDevicefile(ip,context,deviceString,stateCallBack);
                    }
                }).start();
            }

            @Override
            public void error(ACException e) {
                Log.i("cao", "error------" + e.toString());
            }
        });
    }



}
