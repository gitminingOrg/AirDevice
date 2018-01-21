package form;

import java.util.List;

/**
 * Created by XXH on 2017/12/31.
 */
public class OrderItemWrapper {
    private String orderId;
    private List<OrderItemForm> commodities;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemForm> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<OrderItemForm> commodities) {
        this.commodities = commodities;
    }
}
