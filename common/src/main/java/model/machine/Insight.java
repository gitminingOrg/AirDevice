package model.machine;

import model.Entity;

import java.sql.Timestamp;

/**
 * Created by sunshine on 10/12/2017.
 */
public class Insight extends Entity{
    private String insightId;

    private String codeId;

    private String path;

    private Timestamp createAt;

    public String getInsightId() {
        return insightId;
    }

    public void setInsightId(String insightId) {
        this.insightId = insightId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
