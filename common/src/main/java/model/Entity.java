package model;

import java.sql.Timestamp;

public abstract class Entity {
	protected boolean blockFlag;
	
	protected Timestamp createAt;
	
	public Entity() {
		super();
		this.blockFlag = false;
		this.createAt = new Timestamp(System.currentTimeMillis());
	}
}
