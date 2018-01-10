package model.order;

import model.Entity;

/**
 * Created by XXH on 2018/1/9.
 */
public class OrderService extends Entity{

    private String osId;
    private String orderId;
    private String serviceId;

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
