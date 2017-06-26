package model.vip;

import model.Entity;

public class ConsumerAddress extends Entity{
	private String addressId;
	
	private String consumerId;
	
	private String location;
	
	private boolean preferred;
	
	public ConsumerAddress() {
		super();
	}
	
	public ConsumerAddress(String consumerId, String location, boolean preferred) {
		this();
		this.consumerId = consumerId;
		this.location = location;
		this.preferred = preferred;
	}
}
