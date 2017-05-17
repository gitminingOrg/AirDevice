package device.service;

import java.util.List;

import model.CleanerStatus;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.DeviceVipDao;
import bean.DeviceName;
import bean.DeviceStatus;

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
	
	public ReturnCode authorizeDevice(String token){
		return ReturnCode.FAILURE;
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
