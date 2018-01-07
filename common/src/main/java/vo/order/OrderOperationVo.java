package vo.order;

import model.order.OrderStatus;

import java.sql.Timestamp;

/**
 * Created by XXH on 2018/1/3.
 */
public class OrderOperationVo {
    private String operationId;
    private String orderId;
    private OrderStatus preStatus;
    private OrderStatus curStatus;
    private Timestamp createTime;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
