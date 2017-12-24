package model.order;

import model.Entity;

/**
 * Created by hushe on 2017/12/23.
 */
public class OrderBatch extends Entity {
    private String orderId;

    private String orderNo;

    private String buyerName;

    private String price;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String coupon;

    private String goodsTitle;

    private String channel;

    private String payTime;

    public OrderBatch(){
        super();
    }

    public OrderBatch(String[] param){
        this();
        this.orderNo = param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", ""); //订单编号
        this.buyerName = param[1];//买家姓名
        this.price = param[2];//买家实付金额
        this.receiverName = param[3];//收货人姓名
        this.receiverPhone = param[4];//收货人手机号码
        this.receiverAddress = param[5];//收货人地址
        this.goodsTitle = param[6];//商品名称
        this.channel = param[7];//订单渠道
        this.payTime = param[8];//买家付款时间
        this.coupon = param[9];//备注
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

}
