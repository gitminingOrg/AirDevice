package model.order;

import model.Entity;

/**
 * Created by hushe on 2017/12/23.
 */
public class OrderBatch extends Entity {
    private String orderId;

    private String orderNo;

    private String buyerName;

    private String buyerAliAccount;

    private String price;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String coupon;

    private String goodsTitle;

    private String goodsKind;

    private String productSerial;

    private String shipNo;

    private String channel;

    private String payTime;

    private String description;

    private OrderStatus status;

    public OrderBatch(){
        super();
        status = OrderStatus.PAYED;
    }

    public OrderBatch(String[] param){
        this();
        this.orderNo = param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", ""); //订单编号
        this.buyerName = param[1];//买家姓名
        this.price = param[2];//买家实付金额
        this.receiverName = param[3];//收货人姓名
        this.receiverPhone = param[4].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "");//收货人手机号码
        this.receiverAddress = param[5];//收货人地址
        this.coupon = param[6];//优惠码
        this.goodsTitle = param[7];//商品名称
        this.channel = param[8];//订单渠道
        this.payTime = param[9];//买家付款时间
        this.description = param[10];//备注
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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerAliAccount() {
        return buyerAliAccount;
    }

    public void setBuyerAliAccount(String buyerAliAccount) {
        this.buyerAliAccount = buyerAliAccount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsKind() {
        return goodsKind;
    }

    public void setGoodsKind(String goodsKind) {
        this.goodsKind = goodsKind;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
