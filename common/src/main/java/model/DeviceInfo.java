package model;

import java.io.Serializable;

import utils.Constant;
import annotation.Command;

public class DeviceInfo implements Serializable{

	private static final long serialVersionUID = -8263670463998041046L;
	
	private String deviceID;
	@Command(id=0x01, name=Constant.SERVER_IP,length=0x14)
	private String serverIP;
	
	@Command(id=0x02, name=Constant.SERVER_PORT,length=0x05)
	private String serverPort;

	@Command(id=0x03, name=Constant.HEARTBEAT_GAP,length=0x02)
	private int heartbeatGap;
	
	@Command(id = 0xFC, name = Constant.RUNDAY, length = 0x02)
	private int runday;	
	
	@Command(id = 0xFD, name = Constant.DEVICETYPE, length=0x01)
	private int deviceType;
	
	@Command(id = 0xFE, name = Constant.FIRMWARE, length = 0x14)
	private String firmware;

	@Command(id = 0xFF, name = Constant.HARDWARE, length = 0x14)
	private String hardware;
	
	private String updateTime;
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getHeartbeatGap() {
		return heartbeatGap;
	}
	public void setHeartbeatGap(int heartbeatGap) {
		this.heartbeatGap = heartbeatGap;
	}

	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	public String getHardware() {
		return hardware;
	}
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public int getRunday() {
		return runday;
	}
	public void setRunday(int runday) {
		this.runday = runday;
	}

}
