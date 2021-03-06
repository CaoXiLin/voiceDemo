package util;

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
import bean.SceneTableEntity;
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
    public static void loadDevicefile( String deviceString, StateCallBack stateCallBack) {
        JSONObject jsonObject = null;
        List<DeviceTableEntity> deviceList =null;
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
                String s = deviceList.toString();
                Log.i("cxl",Thread.currentThread().getName()+"   loadDevicefile     Name------设备数据>>>>"+s);
                stateCallBack.callbackData(deviceList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 场景文件
    public static void loadScenefile( String sceneString,StateCallBack stateCallBack) {
        JSONObject jsonobject = null;
        List<SceneTableEntity> addSceneList =null;
        try {
            addSceneList = new ArrayList<>();
            jsonobject = new JSONObject(sceneString);
            JSONArray jsonarray = jsonobject.getJSONArray("scene");
            for (int i = 0; i < jsonarray.length(); i++) {
                SceneTableEntity sceneEntity = new SceneTableEntity();
                JSONObject object = jsonarray.getJSONObject(i);
                sceneEntity.scenename = object.optString("scenename");
                sceneEntity.sceneposition = object.optString("sceneposition");
                sceneEntity.location = object.optString("location");
                sceneEntity.sceneid = object.optString("scene_id");

                //判断数据库中是否已经存在此数据，若存在跳过插入
                boolean ishave = false;
                for (int j = 0; j < addSceneList.size(); j++) {
                    if (addSceneList.get(j).sceneid.equals(object.optString("scene_id"))) {
                        ishave = true;
                    }
                }
                if (!ishave) {
                    addSceneList.add(sceneEntity);
                }
                String s = addSceneList.toString();
                Log.i("cxl", "loadScenefile:-----数据 "+s);
            }

            if(stateCallBack!=null){
                stateCallBack.sceneData(addSceneList);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    private static  Runnable mRunnable =null;
    public static void WanDownLoad(final String ip, final StateCallBack stateCallBack) {
        com.accloud.service.ACFileInfo fileInfo = new com.accloud.service.ACFileInfo(ip, Const.PackageFile);
        AC.fileMgr().getDownloadUrl(fileInfo,  24*60*60, new PayloadCallback<String>() {

            @Override
            public void success(final String url) {
                // TODO Auto-generated method stub
                if(mRunnable ==null){
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {

                            DownLoadUtility downLoadUtility = new DownLoadUtility();
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
                            String sceneString = readContent(SDPATH+"pfk/"+Const.SCENEFILE);
                            //保存设备文件到数据库
                            loadDevicefile(deviceString,stateCallBack);
                            loadScenefile(sceneString,stateCallBack);


                        }
                    };
                }
                YohoPoolExecutor.getInstance().getExecutor().execute(mRunnable);
            }

            @Override
            public void error(ACException e) {
                Log.i("cao", "error------" + e.toString());
            }
        });
    }


}
