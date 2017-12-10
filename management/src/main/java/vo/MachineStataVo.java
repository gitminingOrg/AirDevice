package vo;

import java.sql.Timestamp;

/**
 * Created by sunshine on 10/12/2017.
 */
public class MachineStataVo {
    private String machineId;

    private String pm25;

    private boolean isPowerOn;

    private Timestamp createAt;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public boolean isPowerOn() {
        return isPowerOn;
    }

    public void setIsPowerOn(boolean isPowerOn) {
        this.isPowerOn = isPowerOn;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
