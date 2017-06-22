package model.location;

public abstract class Location {
	private int locationId;
	
	private String locationName;
	
	private String nickname;
	
	private String locationPinyin;

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
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
