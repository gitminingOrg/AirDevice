package vo.order;

import model.order.MachineMissionStatus;
import vo.machine.InsightVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by XXH on 2018/1/17.
 */
public class MachineMissionVo {
    private String missionId;
    private String machineId;
    private String missionTitle;
    private String missionContent;
    private String missionRecorder;
    private Timestamp missionDate;
    private boolean blockFlag;
    private Timestamp createTime;
    private List<InsightVo> insightList;


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

    public List<InsightVo> getInsightList() {
        return insightList;
    }

    public void setInsightList(List<InsightVo> insightList) {
        this.insightList = insightList;
    }
}
