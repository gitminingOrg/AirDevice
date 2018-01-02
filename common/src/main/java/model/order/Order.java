package model.order;

import model.Entity;


/***
 * 订单抽象类 最基本的订单属性
 */
public abstract class Order extends Entity{

    protected String orderId;           // 订单id， 自动生成

    protected String orderNo;           // 订单号不重复

    protected OrderStatus orderStatus;  // 订单状态

    protected String description;       // 备注

    public Order() {
        super();
        this.orderStatus = OrderStatus.PAYED;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
