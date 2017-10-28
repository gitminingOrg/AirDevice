package model.machine;

import model.Entity;

public class IdleMachine extends Entity{
	private String imId;
	
	private String uid;
	
	public IdleMachine() {
		super();
	}
	
	public IdleMachine(String uid) {
		this();
		this.uid = uid;
	}

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
