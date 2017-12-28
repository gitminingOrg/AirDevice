package model.order;

import model.Entity;


/***
 * 订单抽象类 最基本的订单属性
 */
public abstract class Order extends Entity{

    protected String orderId;           //订单id， 自动生成

    protected String orderNo;           //订单号   不重复

    protected String buyerName;         // 购买者名字

    protected String price;             // 商品价格

    protected String receiverPhone;     // 手机号

    protected String goodsTitle;        // 购买商品名称

    public Order() {
        super();
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

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }
}
