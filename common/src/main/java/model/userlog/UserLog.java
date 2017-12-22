package model.userlog;

import model.Entity;


public class UserLog extends Entity{
    private String logId;
    private String userId;
    private String target;
    private String message;


    public UserLog(){
        super();
    }

    public UserLog(String userId, String target, String message){
        this();
        this.userId = userId;
        this.target = target;
        this.message = message;
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

}
