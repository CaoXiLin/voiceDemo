package bean;

import org.json.JSONObject;

/**
 * Created by cxl on 2017/11/28.
 * 设备文件
 */

public class DeviceTableEntity {
    public String deviceid;// 设备的id
    public JSONObject deviceattr;// 设备的属性
    public String devicetype;// 设备的英文名
    public String devicename;// 设备的名字


    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setDeviceattr(JSONObject deviceattr) {
        this.deviceattr = deviceattr;
    }


    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }


    public String getDeviceid() {

        return deviceid;
    }

    public JSONObject getDeviceattr() {
        return deviceattr;
    }


    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getDevicetype() {
        return devicetype;
    }


}
