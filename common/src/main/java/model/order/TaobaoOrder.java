package model.order;

import model.Entity;

public class TaobaoOrder extends Entity{
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
	
	private String payTime;
	
	public TaobaoOrder() {
		super();
	}
	
	public TaobaoOrder(String[] param) {
		this();
		this.orderNo = param[0]; //订单编号
		this.buyerName = param[1]; //买家会员名
		this.buyerAliAccount = param[2]; //买家支付宝
		this.price = param[8]; //买家实际支付金额
		this.receiverName = param[12]; //收货人姓名
		this.receiverAddress = param[13]; //收货人地址
		this.receiverPhone = param[16].replaceAll("'", ""); //联系手机
		this.payTime = param[17];
		this.goodsTitle = param[19];
		this.goodsKind = param[20];
		this.coupon = param[23];
		
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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
}
