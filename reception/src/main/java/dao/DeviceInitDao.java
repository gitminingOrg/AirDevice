package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import bean.CityAqi;
import bean.DeviceAir;

@Repository
public class DeviceInitDao extends BaseDaoImpl{
	
	public List<CityAqi> selectRangeCityAqi(String deviceID, String startTime, String endTime){
		if(deviceID == null || startTime == null || endTime == null){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceID", deviceID);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return sqlSession.selectList("aqiData.selectRangeCityAqi",params);
	}
	
	public boolean insertDeviceAirList(List<DeviceAir> deviceAirs){
		if(deviceAirs == null || deviceAirs.size() == 0){
			return true;
		}
		return sqlSession.insert("aqiData.insertDeviceAirList", deviceAirs) > 0;
	}
}
