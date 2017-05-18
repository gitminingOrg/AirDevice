package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import bean.CityAqi;
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
}
