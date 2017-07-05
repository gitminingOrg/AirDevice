package device.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import model.CleanerStatus;
import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;
import model.SupportForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JsonResponseConverter;
import util.ReceptionConstant;
import utils.HttpDeal;
import utils.TimeUtil;
import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;
import bean.Wechat2Device;
import bean.WechatUser;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.ReceptionConfig;
import dao.DeviceChipDao;
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
	
	@Autowired
	private DeviceChipDao deviceChipDao;
	public void setDeviceChipDao(DeviceChipDao deviceChipDao) {
		this.deviceChipDao = deviceChipDao;
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
	
	public ReturnCode checkDeviceShareCode(String token, String deviceID){
		DeviceShareCode deviceShareCode =  deviceVipDao.getDeviceShareCode(token);
		if(deviceShareCode == null){
			return ReturnCode.FAILURE;
		}else if(!deviceShareCode.getDeviceID().equals(deviceID)){
			return ReturnCode.FAILURE;
		}else{
			String current = TimeUtil.getCurrentTime();
			if (deviceShareCode.getExpireTime() == null || current.compareTo(deviceShareCode.getExpireTime()) > 0) {
				return ReturnCode.FAILURE;
			}else if(deviceShareCode.getStatus() != 1){
				return ReturnCode.FAILURE;
			}else{
				return ReturnCode.SUCCESS;
			}
		}
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
			UserDevice originUserDevice = deviceVipDao.getUserDevice(userID, deviceID);
			if (originUserDevice != null && originUserDevice.getRole() <= deviceShareCode.getRole()) {
				return ReturnCode.FORBIDDEN;
			}else if(originUserDevice != null){
				deviceVipDao.disableUserDevice(userID, deviceID);
			}
			//update auth
			deviceShareCode.setAuthID(userID);
			//deviceShareCode.setStatus(0);
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
		DeviceName origin = deviceVipDao.getDeviceName(deviceName.getDeviceID());
		if(origin != null){
			return ReturnCode.FORBIDDEN;
		}
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
	public String getNewChip(String ip){
		ResultMap result = JsonResponseConverter.getDefaultResultMapWithParams(ReceptionConstant.chipIDPath);
		Gson gson = new Gson();
		String idsJson = gson.toJson(result.getContents().get("devices"));
		List<String> sessionIds = gson.fromJson(idsJson, List.class);
		List<String> bindChips = deviceChipDao.getBindedChips();
		for (String chipID : bindChips) {
			String sessionId = "session." + chipID;
			if(sessionIds.contains(sessionId)){
				sessionIds.remove(sessionId);
			}
		}
		for(String sessionId : sessionIds){
			String chipID = sessionId.split("\\.")[1];
			CleanerStatus cleanerStatus = deviceStatusService.getCleanerStatusByChip(chipID);
			if(! Strings.isNullOrEmpty(cleanerStatus.getIp()) && cleanerStatus.getIp().equals(ip)){
				return chipID;
			}
		}
		return null;
	}
	
	public ReturnCode bind(UserDevice ud) {
		UserDevice userDevice =deviceVipDao.getUserDevice(ud.getUserID(), ud.getDeviceID());
		if(userDevice != null){
			return ReturnCode.FORBIDDEN;
		}
		boolean insert =deviceVipDao.insertUserDevice(ud);
		if(insert){
			return ReturnCode.SUCCESS;
		}
		return ReturnCode.FAILURE;
	}
	
	public int getUserDeviceRole(String userID, String deviceID){
		UserDevice userDevice = deviceVipDao.getUserDevice(userID, deviceID);
		if(userDevice == null){
			return 3;
		}else{
			return userDevice.getRole();
		}
	}
	
	public List<WechatUser> getDeviceWechatUser(String deviceID){
		List<WechatUser> result = new ArrayList<WechatUser>();
		//get wechat user id from db
		List<Wechat2Device> wechat2Devices = deviceVipDao.deviceWechat(deviceID);
		if(wechat2Devices == null || wechat2Devices.size() == 0){
			return result;
		}
		//query wechat server for each user's info
		for (Wechat2Device wechat2Device : wechat2Devices) {
			String wechatID = wechat2Device.getWechatID();
			WechatUser wechatUser = getWechatUserByOpenID(wechatID);
			if(wechatUser == null || wechatUser.getOpenid() == null || wechatUser.getErrcode() != null){
				continue;
			}
			wechatUser.setCustomerID(wechat2Device.getCustomerID());
			wechatUser.setPrivilege(wechat2Device.getPrivilege());
			result.add(wechatUser);
		}
		//enrich user
		return result;
	}
	
	public boolean disableUserDevice(String deviceID, String userID, String ownerID){
		//check owner privilege
		UserDevice userDevice = deviceVipDao.getUserDevice(ownerID, deviceID);
		if(userDevice == null){
			return false;
		}
		if(userDevice.getRole() >= 1){
			return false;
		}
		//diable user
		return deviceVipDao.disableUserDevice(userID, deviceID);
	}
	
	public boolean submitSupportForm(SupportForm supportForm){
		if(supportForm == null){
			return false;
		}
		return deviceVipDao.insertSupportForm(supportForm);
	}
	
	private WechatUser getWechatUserByOpenID(String openID){
		String accessToken = ReceptionConfig.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openID+"&lang=zh_CN";
		String result = HttpDeal.getResponse(url);
		String json = result;
		try {
			json = new String(result.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("charset transfer error");
		}
		Gson gson = new GsonBuilder().create();
		WechatUser wechatUser = gson.fromJson(json, WechatUser.class);
		return wechatUser;
	}
}
