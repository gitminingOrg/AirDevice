package form;

import javax.validation.constraints.NotNull;

public class BindDeviceForm {
	
	@NotNull
	private String serial;
	
	private String alias;
	
	@NotNull
	private String mobile;
	
	private String location;

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
