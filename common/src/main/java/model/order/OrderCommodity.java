package model.order;


import model.Entity;

public class OrderCommodity extends Entity{
    private String commodityId;
    private String orderId;
    private String commodityType;
    private String commodityName;
    private double commodityPrice;
    private int commodityQuantity;


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

    public OrderCommodity() {super();}

    public OrderCommodity(String orderId, String commodityType, String commodityName,
                          double commodityPrice, int commodityQuantity) {
        super();
        this.orderId = orderId;
        this.commodityType = commodityType;
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityQuantity = commodityQuantity;
    }
}
