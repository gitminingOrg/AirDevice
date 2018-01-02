package form;

import java.util.List;

/**
 * Created by XXH on 2017/12/31.
 */
public class OrderCommodityWrapper {
    private String OrderId;
    private List<OrderCommodityForm> commodities;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public List<OrderCommodityForm> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<OrderCommodityForm> commodities) {
        this.commodities = commodities;
    }
}
