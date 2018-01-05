package model.order;

import model.Entity;

/**
 * record operation on order
 */
public class OrderOperation extends Entity{
    private String operationId;
    private String orderId;
    private OrderStatus preStatus;
    private OrderStatus curStatus;


    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(OrderStatus preStatus) {
        this.preStatus = preStatus;
    }

    public OrderStatus getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(OrderStatus curStatus) {
        this.curStatus = curStatus;
    }
}
