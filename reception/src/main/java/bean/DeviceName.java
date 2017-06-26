package bean;

public class DeviceName {
	private String deviceID;
	private String name;
	private String phone;
	private String address;
	private int version;
	
	public DeviceName() {
		super();
	}
	
	public DeviceName(String deviceId, String name, String phone, String address) {
		this();
		this.deviceID = deviceId;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
