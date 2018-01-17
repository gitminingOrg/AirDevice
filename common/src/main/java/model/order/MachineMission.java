package model.order;

import model.Entity;
import utils.MachineStatus;

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
    private MachineStatus machineStatus;

    public String getMmId() {
        return mmId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMissionTitle() {
        return missionTitle;
    }

    public String getMissionContent() {
        return missionContent;
    }

    public String getMissionRecorder() {
        return missionRecorder;
    }

    public MachineStatus getMachineStatus() {
        return machineStatus;
    }

    public void setMmId(String mmId) {
        this.mmId = mmId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setMissionTitle(String missionTitle) {
        this.missionTitle = missionTitle;
    }

    public void setMissionContent(String missionContent) {
        this.missionContent = missionContent;
    }

    public void setMissionRecorder(String missionRecorder) {
        this.missionRecorder = missionRecorder;
    }

    public void setMachineStatus(MachineStatus machineStatus) {
        this.machineStatus = machineStatus;
    }

    public MachineMission(String mmId, String orderId, String deviceId, String missionTitle, String missionContent,
                          String missionRecorder, MachineStatus machineStatus) {
        this.mmId = mmId;
        this.orderId = orderId;
        this.deviceId = deviceId;
        this.missionTitle = missionTitle;
        this.missionContent = missionContent;
        this.missionRecorder = missionRecorder;
        this.machineStatus = machineStatus;
    }
}
