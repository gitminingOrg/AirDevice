package form;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by XXH on 2018/1/17.
 */
public class MachineMissionForm {
    private String orderId;
    private String deviceId;
    private String missionTitle;
    private String missionContent;
    private String installType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime missionDate;
    private String missionChannel;
    private int machineStatus;
    private String filePathList;

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

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public int getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(int machineStatus) {
        this.machineStatus = machineStatus;
    }

    public LocalDateTime getMissionDate() {
        return missionDate;
    }

    public void setMissionDate(LocalDateTime missionDate) {
        this.missionDate = missionDate;
    }

    public String getMissionChannel() {
        return missionChannel;
    }

    public void setMissionChannel(String missionChannel) {
        this.missionChannel = missionChannel;
    }

    public String getFilePathList() {
        return filePathList;
    }

    public void setFilePathList(String filePathList) {
        this.filePathList = filePathList;
    }
}
