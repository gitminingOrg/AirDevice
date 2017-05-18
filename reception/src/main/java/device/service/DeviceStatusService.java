package device.service;

import java.lang.reflect.Method;

import model.CleanerStatus;
import model.ResultMap;
import model.ReturnCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import util.JsonResponseConverter;
import util.ReceptionConstant;
import utils.Constant;
import utils.MethodUtil;
import utils.TimeUtil;
import bean.CityAqi;
import bean.UserAction;

import com.google.gson.Gson;

import dao.DeviceStatusDao;

@Service
public class DeviceStatusService {
	private static final Logger LOG = LoggerFactory.getLogger(DeviceStatusService.class);
	private static final int DEFAULT_TIMES = 5;
	private static final int DEFAULT_PERIOD = 1000;

	@Autowired
	@Qualifier("executor")
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private DeviceStatusDao deviceStatusDao;
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
	public CleanerStatus getCleanerStatus(String deviceID, String userID){
		ResultMap result = JsonResponseConverter.getDefaultResultMapWithParams(ReceptionConstant.deviceStatusPath, deviceID);
		if(result.getStatus() != ResultMap.STATUS_SUCCESS){
			return null;
		}else{
			Object obj = result.getContents().get("status");
			Gson gson = new Gson();
			String cleanerJson = gson.toJson(obj);
			CleanerStatus cleanerStatus = gson.fromJson(cleanerJson, CleanerStatus.class);
			return cleanerStatus;
		}
	}
	
	/**
	 * 
	 * @param on
	 * @param deviceID
	 * @param userID
	 * @return
	 */
	public ReturnCode deviceControl(int data, String deviceID, String userID, String command){
		String urlPath = getMappingUrl(command);
		if (urlPath == null) {
			LOG.warn("unknown command type");
			return ReturnCode.FAILURE;
		}
		ResultMap result = JsonResponseConverter.getDefaultResultMapWithParams(ReceptionConstant.powerControlPath, Integer.toString(data), deviceID);
		if(result.getStatus() != ResultMap.STATUS_SUCCESS){
			LOG.warn("user : "+userID + " operate device : " + deviceID + " power failed");
			return ReturnCode.FAILURE;
		}
		
		boolean check = checkStatus(DEFAULT_TIMES, DEFAULT_PERIOD, command, data, deviceID, userID);
		//add user action to db
		executor.execute(new Runnable() {
			@Override
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
		LOG.info("update air condition");
	}
	
	public CityAqi getCityCurrentAqi(String cityName){
		CityAqi cityAqi = deviceStatusDao.getCityAqi(cityName);
		return cityAqi;
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
		while(cursor < times){
			cursor++;
			CleanerStatus cleanerStatus = getCleanerStatus(deviceID, userID);
			Method method;
			try {
				method = cleanerStatus.getClass().getDeclaredMethod(MethodUtil.getFieldGetMethod(field));
				int actual = (int) method.invoke(cleanerStatus);
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
		return false;
	}
}
