package vo.order;

import model.order.OrderStatus;

import java.sql.Timestamp;

/**
 * Created by hushe on 2017/12/8.
 */
public class OrderStaticsVo {
    private String orderId;
    private OrderStatus order_status;
    private Timestamp create_time;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrder_status() {
        return order_status;
    }

    public void setOrder_status(OrderStatus order_status) {
        this.order_status = order_status;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
