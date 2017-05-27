package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CleanerStatus;

import org.springframework.stereotype.Repository;

import util.ReceptionConstant;
import bean.CityAqi;
import bean.DeviceAir;
import bean.DeviceCity;
import bean.UserAction;

@Repository
public class DeviceStatusDao extends BaseDaoImpl{
	public List<UserAction> getUserActions(String userID){
		return sqlSession.selectList("userAction.selectUserActions", userID);
	}
	
	public boolean insertUserAction(UserAction userAction){
		return sqlSession.insert("userAction.insertUserAction", userAction) > 0;
	}
	
	public CityAqi getCityAqi(String cityName){
		return sqlSession.selectOne("aqiData.selectCityCurrentAqi", cityName);
	}
	
	public List<CityAqi> getAllCityAvgAqi(String beginTime, String endTime){
		Map<String, String> params = new HashMap<String, String>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		return sqlSession.selectList("aqiData.selectAllCityAvgAqi", params);
	}
	
	public DeviceCity getDeviceCity(String deviceName){
		return sqlSession.selectOne("aqiData.selectDeviceCity", deviceName);
	}
	
	
	public boolean insertDeviceCity(DeviceCity deviceCity){
		int affect = sqlSession.insert("aqiData.insertDeviceCity", deviceCity);
		return affect > 0;
	}
	
	public boolean disableDeviceCity(String deviceName){
		return sqlSession.update("aqiData.disableDeviceCity", deviceName) >= 0;
	}
	
	public List<String> selectAllActiveDevices(){
		return sqlSession.selectList("aqiData.selectAllDevices");
	}
	
	public boolean insertDeviceStatus(List<CleanerStatus> list){
		return sqlSession.insert("aqiData.insertCleanerStatusList", list) == list.size();
	}
	
	public List<DeviceAir> selectTopNDayStatus(String deviceID){
		return sqlSession.selectList("aqiData.selectTopNDayStatus", deviceID);
	}
}
