package device.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import model.CleanerStatus;
import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JsonResponseConverter;
import util.ReceptionConstant;
import utils.TimeUtil;
import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;

import com.google.gson.Gson;

import dao.DeviceVipDao;

@Service
public class DeviceVipService {
	private static Logger LOG = LoggerFactory.getLogger(DeviceVipService.class);
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
			CleanerStatus cleanerStatus = deviceStatusService.getCleanerStatus(deviceID);
			deviceStatus.setCleanerStatus(cleanerStatus);
		}
		return userDevices;
	}
	
	public ReturnCode authorizeDevice(String token, String userID){
		DeviceShareCode deviceShareCode = deviceVipDao.getDeviceShareCode(token);
		if (deviceShareCode == null) {
			return ReturnCode.FAILURE;
		}else{
			String deviceID = deviceShareCode.getDeviceID();
			//check expire
			String current = TimeUtil.getCurrentTime();
			if (deviceShareCode.getExpireTime() == null || current.compareTo(deviceShareCode.getExpireTime()) > 0) {
				return ReturnCode.FAILURE;
			}
			//get user device auth
			UserDevice originUserDevice = deviceVipDao.getUserDeviceRole(userID, deviceID);
			if (originUserDevice != null && originUserDevice.getRole() <= deviceShareCode.getRole()) {
				return ReturnCode.FORBIDDEN;
			}else if(originUserDevice != null){
				deviceVipDao.disableUserDevice(userID, deviceID);
			}
			//update auth
			deviceShareCode.setAuthID(userID);
			deviceShareCode.setStatus(0);
			boolean result = deviceVipDao.updateDeviceShareCode(deviceShareCode);
			if (! result) {
				return ReturnCode.FAILURE;
			}
			//insert user device
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
	
	/**
	 * generate share code
	 * @param userID
	 * @param deviceID
	 * @param role
	 * @param expireDays
	 * @return
	 */
	public DeviceShareCode generateShareCode(String userID, String deviceID, int role, int expireDays){
		//cal expire time
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, expireDays);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String expireTime = sdf.format(calendar.getTime());
		
		//generate code
		String token = generateNCharString(ReceptionConstant.TOKEN_LENGTH);
		DeviceShareCode deviceShareCode = deviceVipDao.getDeviceShareCode(token);
		while(deviceShareCode != null){
			token = generateNCharString(ReceptionConstant.TOKEN_LENGTH);
			deviceShareCode = deviceVipDao.getDeviceShareCode(token);
		}
		
		DeviceShareCode newCode = new DeviceShareCode();
		newCode.setDeviceID(deviceID);
		newCode.setExpireTime(expireTime);
		newCode.setOwnerID(userID);
		newCode.setRole(role);
		newCode.setToken(token);
		
		deviceVipDao.insertDeviceShareCode(newCode);
		return newCode;
	}
	/**
	 * change device name
	 * @param userID
	 * @param deviceName
	 * @return
	 */
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
	
	public ReturnCode insertDeviceName(DeviceName deviceName){
		//check privilege
		//update deviceName
		boolean result = deviceVipDao.insertDeviceName(deviceName);
		if (result) {
			return ReturnCode.SUCCESS;
		}else {
			return ReturnCode.FAILURE;
		}
	}
	
	
	/**
	 * get device name by device id
	 * @param deviceID
	 * @return
	 */
	public DeviceName getDeviceName(String deviceID){
		DeviceName deviceName = deviceVipDao.getDeviceName(deviceID);
		return deviceName;
	}
	
	/**
	 * get device info of a device
	 * @param userID
	 * @param device
	 * @return
	 */
	public DeviceInfo getDeviceInfo(String userID, String device){
		ResultMap resultMap = JsonResponseConverter.getDefaultResultMapWithParams(ReceptionConstant.deviceInfoPath, device);
		if (resultMap.getStatus() != ResultMap.STATUS_SUCCESS) {
			LOG.warn("search device info failed, id : " + device);
			return null;
		}else {
			Gson gson = new Gson();
			String infoString = gson.toJson(resultMap.getContents().get(ReceptionConstant.DEVICE));
			DeviceInfo deviceInfo = gson.fromJson(infoString, DeviceInfo.class);
			return deviceInfo;
		}
	}
	
	public List<CityList> getAllCities(){
		return deviceVipDao.getAllCityList();
	}
	
	private String generateNCharString(int length){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char next = 'a';
			int offset = random.nextInt(26);
			next+=offset;
			boolean capital = random.nextBoolean();
			if (capital) {
				next = (char) (next - 'a' + 'A');
			}
			sb.append(next);
			
		}
		
		return sb.toString();
	}
}
