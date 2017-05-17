package bean;

import model.CleanerStatus;

public class DeviceStatus {
	private String deviceID;
	private String userID;
	private String deviceName;
	private int role;
	
	private CleanerStatus cleanerStatus;
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public CleanerStatus getCleanerStatus() {
		return cleanerStatus;
	}
	public void setCleanerStatus(CleanerStatus cleanerStatus) {
		this.cleanerStatus = cleanerStatus;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
}
