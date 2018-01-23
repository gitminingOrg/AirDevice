package form;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by XXH on 2018/1/17.
 */
public class MachineMissionForm {
    private String machineId;
    private String missionTitle;
    private String missionContent;
    private String missionRecorder;
    private String machineQrcode;
    private String machineInstallType;
    private String machineProvider;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime missionDate;
    private String filePathList;
    private String machineStatusCode;

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

    public LocalDateTime getMissionDate() {
        return missionDate;
    }

    public void setMissionDate(LocalDateTime missionDate) {
        this.missionDate = missionDate;
    }

    public String getFilePathList() {
        return filePathList;
    }

    public void setFilePathList(String filePathList) {
        this.filePathList = filePathList;
    }

    public String getMachineStatusCode() {
        return machineStatusCode;
    }

    public void setMachineStatusCode(String machineStatusCode) {
        this.machineStatusCode = machineStatusCode;
    }

    public String getMachineQrcode() {
        return machineQrcode;
    }

    public void setMachineQrcode(String machineQrcode) {
        this.machineQrcode = machineQrcode;
    }

    public String getMachineInstallType() {
        return machineInstallType;
    }

    public void setMachineInstallType(String machineInstallType) {
        this.machineInstallType = machineInstallType;
    }

    public String getMachineProvider() {
        return machineProvider;
    }

    public void setMachineProvider(String machineProvider) {
        this.machineProvider = machineProvider;
    }
}
