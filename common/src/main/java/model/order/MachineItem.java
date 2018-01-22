package model.order;

import model.Entity;


/**
 * Created by XXH on 2018/1/20.
 */
public class MachineItem extends Entity{
    private String machineId;
    private String orderItemId;
    private String providerId;
    private String machineCode;
    private String installType;
    private MachineMissionStatus machineMissionStatus;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public MachineMissionStatus getMachineMissionStatus() {
        return machineMissionStatus;
    }

    public void setMachineMissionStatus(MachineMissionStatus machineMissionStatus) {
        this.machineMissionStatus = machineMissionStatus;
    }

    public MachineItem() {
        machineMissionStatus = MachineMissionStatus.PRE_SURVEY;
    }
}
