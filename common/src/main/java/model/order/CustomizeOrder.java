package model.order;

import model.Entity;

public class CustomizeOrder extends Entity{
	private String orderId;

	private String orderNo;

	private String buyerName;

	private double price;

	private String receiverName;

	private String receiverPhone;

	private String receiverAddress;

	private String coupon;

	private String goodsTitle;

	private String productSerial;

	private String shipNo;

	private String payTime;
	
	private String orderChannel;

	private OrderStatus status;
	
	public CustomizeOrder() {
		super();
		status = OrderStatus.PAYED;
	}
	
	public CustomizeOrder(String orderNo, String orderBuyer, double price, String receiverName, String receiverPhone,
			String receiverAddress, String coupon, String goodsTitle, String payTime, String orderChannel) {
		this();
		this.orderNo = orderNo;
		this.buyerName = orderBuyer;
		this.price = price;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverAddress = receiverAddress;
		this.coupon = coupon;
		this.goodsTitle = goodsTitle;
		this.payTime = payTime;
		this.orderChannel = orderChannel;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
