package model.location;

public class City extends Location{
	private Province province;

	public City() {
		super();
	}
	
	public City(Province province, String locationId, String locationName, String nickname, String locationPinyin) {
		this();
		this.province = province;
		this.locationId = locationId;
		this.locationName = locationName;
		this.nickname = nickname;
		this.locationPinyin = locationPinyin;
	}
	
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}
}
