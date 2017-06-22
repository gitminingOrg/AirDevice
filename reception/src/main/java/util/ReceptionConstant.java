package util;

public class ReceptionConstant {
	public static final String domain = "commander.qingair.net";
	public static final String port = "80";
	public static final String deviceStatusPath = "/AirCleanerOperation/device/status/device?token=%s";
	public static final String powerControlPath = "/AirCleanerOperation/device/status/power/%s?token=%s";
	public static final String heatControlPath = "/AirCleanerOperation/device/status/heat/%s?token=%s";
	public static final String uvControlPath = "/AirCleanerOperation/device/status/uv/%s?token=%s";
	public static final String modeControlPath = "/AirCleanerOperation/device/status/mode/%s?token=%s";
	public static final String velocityControlPath = "/AirCleanerOperation/device/status/velocity/%s?token=%s";
	public static final String cycleControlPath = "/AirCleanerOperation/device/status/cycle/%s?token=%s";
	public static final String lightControlPath = "/AirCleanerOperation/device/status/light/%s?token=%s";
	public static final String deviceInfoPath = "/AirCleanerOperation/device/info/device?token=%s";
	
	public static final String STATUS_LIST = "statusList";
	public static final String DEVICE_NAME = "deviceName";
	public static final String CLEANER_STATUS = "cleanerStatus";
	public static final String DEVICE = "device";
	public static final String CITY = "city";
	public static final String CITY_AQI = "cityAqi";
	public static final String CITY_LIST = "cityList";
	public static final String DEVICE_CITY = "deviceCity";
	public static final String AIR_COMPARE = "airCompare";
	
	public static final int TOKEN_LENGTH = 40;
	public static final int DEFAULT_EXPIRE_DAYS = 1;
	public static final int DEFAULT_QR_LENGTH = 200;
	public static final int DEFAULT_TOP_DAY = 7;
}
