package vo.qrcode;

import java.sql.Timestamp;

/**
 * Created by hushe on 2017/12/8.
 */
public class QRCodeStatusVO {
    private String codeId;
    private String codeValue;
    private String modelId;
    private String modelName;
    private boolean delivered;
    private Timestamp scanTime;
    private Timestamp createTime;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Timestamp getScanTime() {
        return scanTime;
    }

    public void setScanTime(Timestamp scanTime) {
        this.scanTime = scanTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
