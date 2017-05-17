package device.service;

import java.util.List;

import model.CleanerStatus;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.TimeUtil;
import dao.DeviceVipDao;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;

@Service
public class DeviceVipService {
	@Autowired
	private DeviceVipDao deviceVipDao;
	public void setDeviceVipDao(DeviceVipDao deviceVipDao) {
		this.deviceVipDao = deviceVipDao;
	}
	@Autowired
	private DeviceStatusService deviceStatusService;
	public void setDeviceStatusService(DeviceStatusService deviceStatusService) {
		this.deviceStatusService = deviceStatusService;
	}

	/**
	 * get the status of a user's all devices
	 * @param userID
	 * @return
	 */
	public List<DeviceStatus> getUserCleaner(String userID){
		List<DeviceStatus> userDevices = deviceVipDao.getUserDevice(userID);
		for (DeviceStatus deviceStatus : userDevices) {
			String deviceID = deviceStatus.getDeviceID();
			CleanerStatus cleanerStatus = deviceStatusService.getCleanerStatus(deviceID, userID);
			deviceStatus.setCleanerStatus(cleanerStatus);
		}
		return userDevices;
	}
	
	public ReturnCode authorizeDevice(String token, String userID){
		DeviceShareCode deviceShareCode = deviceVipDao.getDeviceShareCode(token);
		if (deviceShareCode == null) {
			return ReturnCode.FAILURE;
		}else{
			//check expire
			String current = TimeUtil.getCurrentTime();
			if (deviceShareCode.getExpireTime() == null || current.compareTo(deviceShareCode.getExpireTime()) > 0) {
				return ReturnCode.FAILURE;
			}
			//update auth
			deviceShareCode.setAuthID(userID);
			deviceShareCode.setStatus(0);
			boolean result = deviceVipDao.updateDeviceShareCode(deviceShareCode);
			if (! result) {
				return ReturnCode.FAILURE;
			}
			//insert user device
			String deviceID = deviceShareCode.getDeviceID();
			int role = deviceShareCode.getRole();
			UserDevice userDevice = new UserDevice();
			userDevice.setUserID(userID);
			userDevice.setDeviceID(deviceID);
			userDevice.setRole(role);
			result = deviceVipDao.insertUserDevice(userDevice);
			if (! result) {
				return ReturnCode.FAILURE;
			}
			return ReturnCode.SUCCESS;
		}
	}
	
	public String generateShareCode(String userID, String deviceID){
		return null;
	}
	
	public ReturnCode configDeviceName(String userID, DeviceName deviceName){
		//check privilege
		//update deviceName
		boolean result = deviceVipDao.updateDeviceName(deviceName);
		if (result) {
			return ReturnCode.SUCCESS;
		}else {
			return ReturnCode.FAILURE;
		}
	}
}
