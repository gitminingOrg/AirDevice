package util;

public class ReceptionConstant {
	public static final String domain = "commander.qingair.net";
	public static final String port = "8080";
	public static final String deviceStatusPath = "/AirCleaner/device/status/device?token=%s";
	public static final String powerControlPath = "/AirCleaner/device/status/power/%s?token=%s";
	public static final String heatControlPath = "/AirCleaner/device/status/heat/%s?token=%s";
	public static final String uvControlPath = "/AirCleaner/device/status/UV/%s?token=%s";
	public static final String modeControlPath = "/AirCleaner/device/status/mode/%s?token=%s";
	public static final String velocityControlPath = "/AirCleaner/device/status/velocity/%s?token=%s";
	public static final String cycleControlPath = "/AirCleaner/device/status/cycle/%s?token=%s";
	public static final String lightControlPath = "/AirCleaner/device/status/light/%s?token=%s";
	public static final String deviceInfoPath = "/AirCleaner/device/info/device?token=%s";
	
	public static final String STATUS_LIST = "statusList";
	public static final String DEVICE_NAME = "deviceName";
	public static final String CLEANER_STATUS = "cleanerStatus";
	public static final String DEVICE = "device";
	public static final String CITY = "city";
	public static final String DEVICE_CITY = "deviceCity";
	
	public static final int TOKEN_LENGTH = 40;
	public static final int DEFAULT_EXPIRE_DAYS = 1;
	public static final int DEFAULT_QR_LENGTH = 200;
}
