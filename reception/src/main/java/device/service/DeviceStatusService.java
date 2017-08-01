package device.service;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import model.CleanerStatus;
import model.ResultMap;
import model.ReturnCode;
import model.device.DeviceChip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import util.JsonResponseConverter;
import util.ReceptionConstant;
import util.TokenGenerator;
import utils.Constant;
import utils.MethodUtil;
import utils.TimeUtil;
import bean.AirCompareVO;
import bean.CityAqi;
import bean.DeviceAir;
import bean.DeviceCity;
import bean.UserAction;
import bean.UserDevice;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;

import dao.DeviceChipDao;
import dao.DeviceStatusDao;
import dao.DeviceVipDao;

@Service
public class DeviceStatusService {
	private static final Logger LOG = LoggerFactory.getLogger(DeviceStatusService.class);
	private static final int DEFAULT_TIMES = 5;
	private static final int DEFAULT_PERIOD = 1000;
	@NotNull
	LoadingCache<String, String> cache;
	@PostConstruct
	public void initCache(){
		cache = CacheBuilder.newBuilder().
				expireAfterWrite(2, TimeUnit.HOURS).maximumSize(100000).build(new CacheLoader<String, String>(){
					@Override
					public String load(String key) throws Exception {
						return null;
				}});
	}

	@Autowired
	@Qualifier("executor")
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private DeviceStatusDao deviceStatusDao;
	
	@Autowired
	private DeviceChipDao deviceChipDao;
	
	@Autowired
	private DeviceVipDao deviceVipDao;
	
	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

	public void setDeviceStatusDao(DeviceStatusDao deviceStatusDao) {
		this.deviceStatusDao = deviceStatusDao;
	}

	/**
	 * get current status of a device
	 * @param deviceID
	 * @return
	 */
	public CleanerStatus getCleanerStatus(String deviceID){
		String chipID = deviceIDToChipID(deviceID);
		return getCleanerStatusByChip(chipID);
	}
	
	public CleanerStatus getCleanerStatusByChip(String chipID){
		if(chipID == null){
			return null;
		}
		ResultMap result = JsonResponseConverter.getDefaultResultMapWithParams(ReceptionConstant.deviceStatusPath, chipID);
		if(result.getStatus() != ResultMap.STATUS_SUCCESS){
			return null;
		}else{
			Object obj = result.getContents().get("status");
			Gson gson = new Gson();
			String cleanerJson = gson.toJson(obj);
			LOG.info(cleanerJson);
			CleanerStatus cleanerStatus = gson.fromJson(cleanerJson, CleanerStatus.class);
			return cleanerStatus;
		}
	}
	
	public String generateDeviceReadToken(String deviceID){
		String token = TokenGenerator.generateNCharString(ReceptionConstant.TOKEN_LENGTH);
		cache.put(token, deviceID);
		return token;
	}
	
	
	public String getDeviceIDByToken(String token){
		if(token == null){
			return null;
		}
		if(cache == null){
			cache = CacheBuilder.newBuilder().
					expireAfterWrite(5, TimeUnit.DAYS).maximumSize(1000000).build(new CacheLoader<String, String>(){
						@Override
						public String load(String key) throws Exception {
							return null;
					}});
		}
		try {
			return cache.get(token);
		} catch (ExecutionException e) {
			LOG.error("get device by token failed",e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param on
	 * @param deviceID
	 * @param userID
	 * @return
	 */
	public ReturnCode deviceControl(final int data, final String deviceID, final String userID, final String command){
		UserDevice userDevice = deviceVipDao.getUserDevice(userID, deviceID);
		if(userDevice == null || userDevice.getRole() > 1){
			return ReturnCode.FORBIDDEN;
		}
		String urlPath = getMappingUrl(command);
		if (urlPath == null) {
			LOG.warn("unknown command type");
			return ReturnCode.FAILURE;
		}
		String chipID = deviceIDToChipID(deviceID);
		if(chipID == null){
			return ReturnCode.FAILURE;
		}
		ResultMap result = JsonResponseConverter.getDefaultResultMapWithParams(urlPath, Integer.toString(data), chipID);
		if(result.getStatus() != ResultMap.STATUS_SUCCESS){
			LOG.warn("user : "+userID + " operate device : " + deviceID + " power failed");
			return ReturnCode.FAILURE;
		}
		
		boolean check = checkStatus(DEFAULT_TIMES, DEFAULT_PERIOD, command, data, deviceID, userID);
		//add user action to db
		executor.execute(new Runnable() {
			
			public void run() {
				UserAction userAction = new UserAction();
				userAction.setCommand(command);
				userAction.setData(data);
				userAction.setDeviceID(deviceID);
				userAction.setUserID(userID);
				userAction.setTime(TimeUtil.getCurrentTime());
				deviceStatusDao.insertUserAction(userAction);
			}
		});
		if(check){
			return ReturnCode.SUCCESS;
		}
		return ReturnCode.FAILURE;
	}
	
	public void updateAirCondition(){
		//get all device
		List<String> devices = deviceStatusDao.selectAllActiveDevices();
		//get device aqi data
		List<CleanerStatus> cleanerStatusList = new ArrayList<CleanerStatus>();
		for (String deviceID : devices) {
			CleanerStatus cleanerStatus = getCleanerStatus(deviceID);
			if(cleanerStatus != null && cleanerStatus.getDeviceID() != null){
				cleanerStatusList.add(cleanerStatus);
			}
		}
		//insert device aqi data
		boolean update = deviceStatusDao.insertDeviceStatus(cleanerStatusList);
	}
	
	public CityAqi getCityCurrentAqi(String cityName){
		CityAqi cityAqi = deviceStatusDao.getCityAqi(cityName);
		return cityAqi;
	}
	
	public ReturnCode setDeviceCity(String userID, String deviceID, String cityName){
		if(Strings.isNullOrEmpty(cityName)){
			return ReturnCode.FAILURE;
		}
		DeviceCity deviceCity = new DeviceCity();
		deviceCity.setCity(cityName);
		deviceCity.setDeviceID(deviceID);
		DeviceCity origin = deviceStatusDao.getDeviceCity(deviceID);
		if(origin != null && origin.getCity().equals(cityName)){
			return ReturnCode.SUCCESS;
		}
		boolean disable = deviceStatusDao.disableDeviceCity(deviceID);
		boolean insert = deviceStatusDao.insertDeviceCity(deviceCity);
		if (insert) {
			return ReturnCode.SUCCESS;
		}else {
			return ReturnCode.FAILURE;
		}
	}
	
	public AirCompareVO getAirCompareVO(String deviceID){
		List<String> dates = TimeUtil.getLastWeekdays();
		List<Integer> insides = new ArrayList<Integer>();
		List<Integer> outsides = new ArrayList<Integer>();
		
		List<DeviceAir> deviceAirs = deviceStatusDao.selectTopNDayStatus(deviceID);
		if (deviceAirs == null || deviceAirs.size() == 0) {
			return null;
		}else {
			for (DeviceAir deviceAir : deviceAirs) {
				dates.add(deviceAir.getDate());
				insides.add(deviceAir.getInsideAir());
				outsides.add(deviceAir.getOutsideAir());
			}
			AirCompareVO airCompareVO = new AirCompareVO();
			airCompareVO.setDates(dates);
			airCompareVO.setInsides(insides);
			airCompareVO.setOutsides(outsides);
			return airCompareVO;
		}
	}
	
	public DeviceCity getDeviceCity(String userID, String deviceID){
		DeviceCity deviceCity = deviceStatusDao.getDeviceCity(deviceID);
		return deviceCity;
	}
	
	public void updateTodayDeviceAir(){
		
	}
	
	private String getMappingUrl(String command){
		if (command.equals(Constant.POWER)) {
			return ReceptionConstant.powerControlPath;
		}else if(command.equals(Constant.WORKMODE)){
			return ReceptionConstant.modeControlPath;
		}else if(command.equals(Constant.VELOCITY)) {
			return ReceptionConstant.velocityControlPath;
		}else if(command.equals(Constant.UV)){
			return ReceptionConstant.uvControlPath;
		}else if (command.equals(Constant.HEAT)) {
			return ReceptionConstant.heatControlPath;
		}else if (command.equals(Constant.CYCLE)){
			return ReceptionConstant.cycleControlPath;
		}else if (command.equals(Constant.LIGHT)){
			return ReceptionConstant.lightControlPath;
		}else{
			return null;
		}
	}
	
	
	/**
	 * check status several times
	 * @param times
	 * @param period
	 * @param field
	 * @param expect
	 * @param deviceID
	 * @param userID
	 * @return
	 */
	private boolean checkStatus(int times, int period, String field, int expect, String deviceID, String userID){
		//current try
		int cursor = 0;
		String statusTime = null;
		while(cursor < times){
			cursor++;
			CleanerStatus cleanerStatus = getCleanerStatus(deviceID);
			if(cleanerStatus != null){
				statusTime = cleanerStatus.getTime();
			}
			Method method;
			try {
				method = cleanerStatus.getClass().getDeclaredMethod(MethodUtil.getFieldGetMethod(field));
				
				int actual = (int) method.invoke(cleanerStatus);
				LOG.info(method.getName() + actual  + "  " + expect);
				if (actual == expect) {
					return true;
				}
			} catch (Exception e) {
				LOG.error("REFLECT METHOD INVOKE FAILED!", e);
			}
			
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				LOG.error("Thread sleep failed", e);
			}
		}
		if(statusTime != null && compareCurrentTime(statusTime, ReceptionConstant.DEFAULT_GAP_SECOND)){
			return true;
		}
		return false;
	}
	
	public String deviceIDToChipID(String deviceID){
		return deviceStatusDao.getChipIDByDeviceID(deviceID);
	}
	
	public boolean bindDevice2Chip(DeviceChip dc) {
		return deviceChipDao.insert(dc);
	}
	
	private boolean compareCurrentTime(String time, int maxDiffSecond){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date inputDate = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, 0 - maxDiffSecond);
			Date thresholdTime = calendar.getTime();
			
			if(inputDate.after(thresholdTime)){
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
