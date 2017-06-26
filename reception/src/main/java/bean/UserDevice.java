package bean;

public class UserDevice {
	private String userID;
	private String deviceID;
	private int role;
	
	public UserDevice() {
		super();
	}
	
	public UserDevice(String userId, String deviceId) {
		this();
		this.userID = userId;
		this.deviceID = deviceId;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
}
