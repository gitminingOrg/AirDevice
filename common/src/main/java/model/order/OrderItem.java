package model.order;


import model.Entity;

public class OrderItem extends Entity{
    private String orderItemId;
    private String orderId;
    private String commodityId;
    private int commodityQuantity;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public int getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(int commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    public OrderItem(String orderId, String commodityId, int commodityQuantity) {
        this.orderId = orderId;
        this.commodityId = commodityId;
        this.commodityQuantity = commodityQuantity;
    }
}
