package model.location;

public class Province extends Location{
	public Province() {
		super();
	}
	
	public Province(String locationId, String locationName, String nickname, String locationPinyin) {
		this();
		this.locationId = locationId;
		this.locationName = locationName;
		this.nickname = nickname;
		this.locationPinyin = locationPinyin;
	}
}
