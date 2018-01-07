package vo.order;

import java.sql.Timestamp;


public class OrderCommodityVo {
    private String commodityId;
    private String orderId;
    private String commodityType;
    private String commodityName;
    private double commodityPrice;
    private int commodityQuantity;
    private String commodityQrcode;
    private Boolean blockFlag;
    private Timestamp createTime;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public int getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(int commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    public String getCommodityQrcode() {
        return commodityQrcode;
    }

    public void setCommodityQrcode(String commodityQrcode) {
        this.commodityQrcode = commodityQrcode;
    }

    public Boolean getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(Boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
