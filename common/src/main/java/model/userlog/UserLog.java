package model.userlog;

import model.Entity;

import java.security.Timestamp;

public class UserLog extends Entity{
    private String logId;
    private String userId;
    private String target;
    private String message;
    private int blockFlag;
    private Timestamp createTime;

    public UserLog(){

        super();
    }

    public UserLog(String logId, String userId, String target, String message, int blockFlag, Timestamp createTime){
        this();
        this.logId = logId;
        this.userId = userId;
        this.target = target;
        this.message = message;
        this.blockFlag = blockFlag;
        this.createTime = createTime;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(int blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
