package model.location;

public abstract class Location {
	protected String locationId;
	
	protected String locationName;
	
	protected String nickname;
	
	protected String locationPinyin;

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLocationPinyin() {
		return locationPinyin;
	}

	public void setLocationPinyin(String locationPinyin) {
		this.locationPinyin = locationPinyin;
	}	
}
