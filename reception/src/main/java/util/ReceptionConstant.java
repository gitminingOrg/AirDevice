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
	public static final String STATUS_LIST = "statusList";
	public static final String DEVICE_NAME = "deviceName";
	public static final String CLEANER_STATUS = "cleanerStatus";
}
