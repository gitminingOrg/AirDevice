package model.order;

import model.Entity;

/**
 * Created by XXH on 2018/1/17.
 */
public class MachineMission extends Entity{

    private String mmId;
    private String orderId;
    private String deviceId;
    private String missionTitle;
    private String missionContent;
    private String missionRecorder;
    private MachineMissionStatus machineMissionStatus;

    public String getMmId() {
        return mmId;
    }

    public void setMmId(String mmId) {
        this.mmId = mmId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public MachineMissionStatus getMachineMissionStatus() {
        return machineMissionStatus;
    }

    public void setMachineMissionStatus(MachineMissionStatus machineMissionStatus) {
        this.machineMissionStatus = machineMissionStatus;
    }

    public MachineMission(String orderId, String deviceId, String missionTitle, String missionContent,
                          String missionRecorder, MachineMissionStatus machineMissionStatus) {
        this.orderId = orderId;
        this.deviceId = deviceId;
        this.missionTitle = missionTitle;
        this.missionContent = missionContent;
        this.missionRecorder = missionRecorder;
        this.machineMissionStatus = machineMissionStatus;
    }
}
