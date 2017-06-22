package model.location;

public class City extends Location{
	private Province province;

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}
}
