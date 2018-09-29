package bean;

/**
 * Created by cxl on 2017/11/29.
 * 场景文件
 */

public class SceneTableEntity {
   public String scenename;
    public String sceneposition;  // 根据值查找场景的图标
    public String location; // 场景的位置
    public String sceneid; // 场景的唯一值， 时间戳
    public String roomPosition; //app 中 场景图标的位置


    @Override
    public String toString() {
        return "SceneTableEntity [scenename=" + scenename + ", sceneposition=" + sceneposition
                + ", location=" + location + ", sceneid=" + sceneid + "]";
    }
}
