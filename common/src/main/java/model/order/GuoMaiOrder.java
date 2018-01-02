package model.order;

import java.sql.Timestamp;
import java.util.List;

public class GuoMaiOrder extends Order {

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
    private List<OrderCommodity> commodityList;

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

    public List<OrderCommodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<OrderCommodity> commodityList) {
        this.commodityList = commodityList;
    }



    public GuoMaiOrder(){
        super();
    }

    public GuoMaiOrder(String orderNo, String buyerName, String buyerAccount, String receiverName,
                       String receiverPhone, double orderPrice, String province, String city, String district,
                       String receiverAddress, String orderChannel, String orderCoupon,
                       Timestamp orderTime, String description)
    {
        super();
        this.orderNo = orderNo;
        this.buyerName = buyerName;
        this.buyerAccount = buyerAccount;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.orderPrice = orderPrice;
        this.province = province;
        this.city = city;
        this.district = district;
        this.receiverAddress = receiverAddress;
        this.orderChannel = orderChannel;
        this.orderCoupon = orderCoupon;
        this.orderTime = orderTime;
        this.description = description;

        this.orderDiversion = "";
        this.shipNo = "";
    }

    public GuoMaiOrder(String[] param){
        this();
//        this.orderNo = param[0].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", ""); //订单编号
//        this.buyerName = param[1];//买家姓名
//        this.price = param[2];//买家实付金额
//        this.receiverName = param[3];//收货人姓名
//        this.receiverPhone = param[4].replaceAll("'", "").replaceAll("\"", "").replaceAll("=", "");//收货人手机号码
//        this.receiverAddress = param[5];//收货人地址
//        this.coupon = param[6];//优惠码
//        this.goodsTitle = param[7];//商品名称
//        this.channel = param[8];//订单渠道
//        this.payTime = param[9];//买家付款时间
//        this.description = param[10];//备注
    }

}
