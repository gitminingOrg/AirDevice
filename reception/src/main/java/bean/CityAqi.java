package bean;

public class CityAqi {
	private String cityName;
	private String cityPinyin;
	private String time;
	private int aqiData;
	private String aqiGrade;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public int getAqiData() {
		return aqiData;
	}
	public void setAqiData(int aqiData) {
		this.aqiData = aqiData;
	}
	public String getAqiGrade() {
		return aqiGrade;
	}
	public void setAqiGrade(String aqiGrade) {
		this.aqiGrade = aqiGrade;
	}
	public String getCityPinyin() {
		return cityPinyin;
	}
	public void setCityPinyin(String cityPinyin) {
		this.cityPinyin = cityPinyin;
	}
	
}
