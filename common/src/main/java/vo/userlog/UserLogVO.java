package vo.userlog;

import java.sql.Timestamp;


public class UserLogVO {
    private String logId;
    private String userId;
    private String target;
    private String message;
    private boolean blockFlag;
    private Timestamp createAt;

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

    public boolean isBlockFlag() { return blockFlag; }

    public void setBlockFlag(boolean blockFlag) { this.blockFlag = blockFlag; }

    public Timestamp getCreateAt() { return createAt; }

    public void setCreateAt(Timestamp createAt) { this.createAt = createAt; }
}
