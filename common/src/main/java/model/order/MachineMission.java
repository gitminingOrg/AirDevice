package model.order;

import model.Entity;

import java.sql.Timestamp;

/**
 * Created by XXH on 2018/1/17.
 */
public class MachineMission extends Entity{

    private String missionId;
    private String machineId;
    private String missionTitle;
    private String missionContent;
    private String missionRecorder;
    private Timestamp missionDate;

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
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

    public Timestamp getMissionDate() {
        return missionDate;
    }

    public void setMissionDate(Timestamp missionDate) {
        this.missionDate = missionDate;
    }

    public MachineMission(String machineId, String missionTitle, String missionContent,
                          String missionRecorder, Timestamp missionDate)
    {
        super();
        this.machineId = machineId;
        this.missionTitle = missionTitle;
        this.missionContent = missionContent;
        this.missionRecorder = missionRecorder;
        this.missionDate = missionDate;
    }
}
