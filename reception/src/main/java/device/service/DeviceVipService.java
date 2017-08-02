package device.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.CleanerStatus;
import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;
import model.SupportForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import util.JsonResponseConverter;
import util.ReceptionConstant;
import util.TokenGenerator;
import utils.HttpDeal;
import utils.TimeUtil;
import bean.CityList;
import bean.DeviceCity;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;
import bean.Wechat2Device;
import bean.WechatUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import config.ReceptionConfig;
import dao.DeviceAttributeDao;
import dao.DeviceChipDao;
import dao.DeviceVipDao;

@Service
public class DeviceVipService {
	private static Logger LOG = LoggerFactory.getLogger(DeviceVipService.class);
	@Autowired
	private DeviceVipDao deviceVipDao;
	@Autowired
	private DeviceAttributeDao deviceAttributeDao;
	@Autowired
	private DeviceStatusService deviceStatusService;
	@Autowired
	private DeviceChipDao deviceChipDao;

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
			
			//update user location
			DeviceCity deviceCity = deviceStatusService.getDeviceCity(userID, deviceID);
			if(deviceCity != null){
				String city = deviceCity.getCity();
				updateUserLocation(userID, city);
			}
			
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
		String token = TokenGenerator.generateNCharString(ReceptionConstant.TOKEN_LENGTH);
		DeviceShareCode deviceShareCode = deviceVipDao.getDeviceShareCode(token);
		while(deviceShareCode != null){
			token = TokenGenerator.generateNCharString(ReceptionConstant.TOKEN_LENGTH);
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
		if(sessionIds == null || sessionIds.size() == 0){
			return null;
		}
		//if only one new session, that's it
		if(sessionIds.size() == 1){
			String chipID = sessionIds.get(0).split("\\.")[1];
			return chipID;
		}
		//else compare ip, find the most likely
		String suspectID = null;
		int sim = -1;
		
		for(String sessionId : sessionIds){
			String chipID = sessionId.split("\\.")[1];
			CleanerStatus cleanerStatus = deviceStatusService.getCleanerStatusByChip(chipID);
			if(cleanerStatus == null){
				continue;
			}
			//if ip same, that's it
			if(! Strings.isNullOrEmpty(cleanerStatus.getIp()) && cleanerStatus.getIp().equals(ip)){
				return chipID;
			}
			
			//else cal ip sim, get the most like one
			int similarity = calIPSimilarity(ip, cleanerStatus.getIp());
			if(similarity > sim){
				sim = similarity;
			}
			suspectID = chipID;
		}
		return suspectID;
	}
	
	private int calIPSimilarity(String ip1, String ip2){
		if(ip1 == null || ip2 == null){
			return 0;
		}
		int result = 0;
		int score = 8;
		String[] items1 = ip1.split("\\.");
		String[] items2 = ip2.split("\\.");
		if(items1.length != 4 || items2.length != 4){
			return 0;
		}
		for (int i = 0; i < items1.length; i++) {
			if(items1[i].equals(items2[i])){
				result+=score;
			}
			score = score / 2;
		}
		return result;
	}
	
	public ReturnCode bind(UserDevice ud) {
		UserDevice userDevice =deviceVipDao.getUserDevice(ud.getUserID(), ud.getDeviceID());
		if(userDevice != null){
			return ReturnCode.FORBIDDEN;
		}
		boolean insert =deviceVipDao.insertUserDevice(ud);
		if(insert){
			//绑定成功，更新二维码扫码时间
			String scanTime = TimeUtil.getCurrentTime();
			deviceVipDao.updateQRScanTime(ud.getDeviceID(), scanTime);
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
	
	public boolean updateUserLocation(String userID, String cityPinyin){
		deviceVipDao.disableUserLocation(userID);
		return deviceVipDao.addUserLocation(userID, cityPinyin);
	}
	
	public String getUserCity(String userID){
		return deviceVipDao.getUserCity(userID);
	}
	
	public boolean checkSubscribe(String openID){
		String accessToken = ReceptionConfig.getAccessToken();
		String next_openid = "start_default";
		while(!Strings.isNullOrEmpty(next_openid)){
			String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken;
			if(!next_openid.equals("start_default")){
				url = url + "&next_openid="+next_openid;
			}
			String json = HttpDeal.getResponse(url);
			JSONObject jsonObject = JSONObject.parseObject(json);
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("openid");
			if(jsonArray.size() == 0){
				return false;
			}else if(jsonArray.contains(openID)){
				return true;
			}
			next_openid = jsonObject.getString("next_openid");
		}
		return false;
	}
}
