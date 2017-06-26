package model.device;

import java.sql.Timestamp;

public class DeviceChip {
	private String bindId;
	
	private String deviceId;
	
	private String chipId;
	
	private int status = 1;
	
	private Timestamp updateAt;
	
	public DeviceChip() {
		super();
		this.updateAt = new java.sql.Timestamp(System.currentTimeMillis());
	}
	
	public DeviceChip(String deviceId, String chipId) {
		this();
		this.deviceId = deviceId;
		this.chipId = chipId;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
}
