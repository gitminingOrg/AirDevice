package model.systemlog;

import model.Entity;

/**
 * Created by hushe on 2017/11/16.
 */
public class SystemLog extends Entity {

    private String logId;
    private String target;
    private String message;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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
}
