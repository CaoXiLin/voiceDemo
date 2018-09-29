package util;

import java.util.List;

import bean.DeviceTableEntity;
import bean.SceneTableEntity;

/**
 * Created by cxl onGatewayDataBack 2017/12/4.
 */

public interface StateCallBack {
    /**
     * : 1:success   2:fail
     **/
    public void callbackData(List<DeviceTableEntity> data );

    void  onSuccess(String Success,String ip);
    void  sceneData(List<SceneTableEntity> sceneData);

    void  failure(String error);
}
