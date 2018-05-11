package util;

import java.util.List;

import bean.DeviceTableEntity;

/**
 * Created by cxl on 2017/12/4.
 */

public interface StateCallBack {
    /**
     * : 1:success   2:fail
     **/
    public void callbackData(List<DeviceTableEntity> data );

    void  onSuccess(String Success,String ip);

    void  failure(String error);
}
