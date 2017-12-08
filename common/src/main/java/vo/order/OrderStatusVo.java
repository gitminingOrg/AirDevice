package vo.order;

import model.order.OrderStatus;

import java.sql.Timestamp;

/**
 * Created by hushe on 2017/12/8.
 */
public class OrderStatusVo {
    private String orderId;
    private OrderStatus status;
    private Timestamp createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
