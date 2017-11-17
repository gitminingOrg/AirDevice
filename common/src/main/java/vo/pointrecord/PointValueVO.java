package vo.pointrecord;


import java.sql.Timestamp;

public class PointValueVO {
    private String recordId;
    private String orderId;
    private String consumerId;
    private String modelCode;
    private int modelBonus;
    private Timestamp createAt;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public int getModelBonus() {
        return modelBonus;
    }

    public void setModelBonus(int modelBonus) {
        this.modelBonus = modelBonus;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
