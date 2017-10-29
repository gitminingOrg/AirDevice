package finley.monitor.vo;

public class CityPM25Vo {
	private String cityPinyin;
	
	private String cityName;
	
	private String pm25;
	
	private String cityAqi;
	
	private String quality;
	
	private String time;

	public String getCityPinyin() {
		return cityPinyin;
	}

	public void setCityPinyin(String cityPinyin) {
		this.cityPinyin = cityPinyin;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	
	public String getCityAqi() {
		return cityAqi;
	}

	public void setCityAqi(String cityAqi) {
		this.cityAqi = cityAqi;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
