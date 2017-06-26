package model.vip;

import model.Entity;

public class Consumer extends Entity{
	private String consumerId;
	
	private String name;

	private String wechat;
	
	public Consumer() {
		super();
	}
	
	public Consumer(String wechat) {
		this();
		this.wechat = wechat;
	}
	
	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
}
