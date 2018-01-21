package vo.order;

import model.order.OrderItem;
import model.order.OrderStatus;

import java.sql.Timestamp;
import java.util.List;


public class GuoMaiOrderVo {
    private String orderId;
    private String orderNo;
    private OrderStatus OrderStatus;
    private String description;

    private String buyerName;
    private String buyerAccount;
    private String receiverName;
    private String receiverPhone;
    private double orderPrice;
    private String province;
    private String city;
    private String district;
    private String receiverAddress;
    private String orderChannel;
    private String orderDiversion;
    private String orderCoupon;
    private Timestamp orderTime;
    private String shipNo;
    private String serviceName;
    private List<OrderItem> commodityList;

    private boolean blockFlag;
    private Timestamp createTime;

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
        return OrderStatus;
    }

    public void setOrderStatus(OrderStatus OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getOrderDiversion() {
        return orderDiversion;
    }

    public void setOrderDiversion(String orderDiversion) {
        this.orderDiversion = orderDiversion;
    }

    public String getOrderCoupon() {
        return orderCoupon;
    }

    public void setOrderCoupon(String orderCoupon) {
        this.orderCoupon = orderCoupon;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public List<OrderItem> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<OrderItem> commodityList) {
        this.commodityList = commodityList;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
