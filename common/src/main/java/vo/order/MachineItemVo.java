package vo.order;

import model.order.MachineMissionStatus;
import utils.MachineStatus;

import java.sql.Timestamp;

/**
 * Created by XXH on 2018/1/20.
 */
public class MachineItemVo {

    private String machineId;
    private String orderId;
    private String orderItemId;
    private String providerName;
    private String machineCode;
    private String installType;
    private MachineMissionStatus machineMissionStatus;
    private boolean blockFlag;
    private Timestamp createTime;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
