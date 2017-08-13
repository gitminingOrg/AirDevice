package bean;

public class DeviceName {
	private String deviceID;
	private String name;
	private String owner;
	private String phone;
	private String provinceID;
	private String cityID;
	private String address;
	private int version;
	
	public DeviceName() {
		super();
	}

	public DeviceName(String deviceID, String name, String owner, String phone,
			String provinceID, String cityID, String address, int version) {
		super();
		this.deviceID = deviceID;
		this.name = name;
		this.owner = owner;
		this.phone = phone;
		this.provinceID = provinceID;
		this.cityID = cityID;
		this.address = address;
		this.version = version;
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

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
