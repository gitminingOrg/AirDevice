package vo.order;


import vo.machine.InsightVo;

import java.sql.Timestamp;
import java.util.List;

public class OrderMissionVo {

    private String missionId;

    private String orderId;

    private String missionTitle;

    private String missionContent;

    private String missionRecorder;

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMissionTitle() {
        return missionTitle;
    }

    public void setMissionTitle(String missionTitle) {
        this.missionTitle = missionTitle;
    }

    public String getMissionContent() {
        return missionContent;
    }

    public void setMissionContent(String missionContent) {
        this.missionContent = missionContent;
    }

    public String getMissionRecorder() {
        return missionRecorder;
    }

    public void setMissionRecorder(String missionRecorder) {
        this.missionRecorder = missionRecorder;
    }

}
