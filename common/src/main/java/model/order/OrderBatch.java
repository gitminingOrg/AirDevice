package model.order;


public class OrderBatch extends Order {

    private String buyerAccount;

    private String receiverName;

    private String receiverAddress;

    private String coupon;

    private String goodsKind;

    private String productSerial;

    private String shipNo;

    private String channel;

    private String payTime;

    private String description;

    private OrderStatus status;

    public String getBuyerAliAccount() {
        return buyerAccount;
    }

    public void setBuyerAliAccount(String buyerAliAccount) {
        this.buyerAccount = buyerAliAccount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public OrderBatch(){
        super();
        status = OrderStatus.PAYED;
        buyerAccount = "无账号";
        goodsKind = "无类别";
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

}
