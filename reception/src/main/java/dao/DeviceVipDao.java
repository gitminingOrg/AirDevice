package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.SupportForm;

import org.springframework.stereotype.Repository;

import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;
import bean.Wechat2Device;

@Repository
public class DeviceVipDao extends BaseDaoImpl{
	public List<DeviceStatus> getUserDevice(String userID){
		return sqlSession.selectList("userVip.getUserDevice", userID);
	}
	
	public DeviceName getDeviceName(String deviceID){
		return sqlSession.selectOne("userVip.selectDeviceName", deviceID);
	}
	
	public boolean updateDeviceName(DeviceName deviceName){
		return sqlSession.update("userVip.updateDeviceName", deviceName) > 0;
	}
	
	public boolean insertDeviceName(DeviceName deviceName){
		return sqlSession.insert("userVip.insertDeviceName", deviceName) > 0;
	}
	
	public DeviceShareCode getDeviceShareCode(String token){
		return sqlSession.selectOne("userVip.getDeviceShareCode", token);
	}
	
	public boolean insertDeviceShareCode(DeviceShareCode deviceShareCode){
		return sqlSession.insert("userVip.insertDeviceShareCode", deviceShareCode) > 0;
	}
	
	public boolean updateDeviceShareCode(DeviceShareCode deviceShareCode){
		return sqlSession.update("userVip.updateDeviceShareCode", deviceShareCode) > 0;
	}
	
	public boolean insertUserDevice(UserDevice userDevice){
		return sqlSession.insert("userVip.insertNewUserDevice", userDevice) > 0;
	}
	
	public List<CityList> getAllCityList(){
		return sqlSession.selectList("aqiData.selectAllCities");
	}
	
	public boolean disableUserDevice(String userID, String deviceID){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userID", userID);
		params.put("deviceID", deviceID);
		return sqlSession.update("userVip.disableUserDevice", params) >= 0;
	}
	
	public boolean insertSupportForm(SupportForm supportForm){
		return sqlSession.insert("userVip.insertSupportForm", supportForm) > 0;
	}
	
	public List<Wechat2Device> deviceWechat(String deviceID){
		return sqlSession.selectList("userVip.deviceWechat",deviceID);
	}
	
	public UserDevice getUserDevice(String userID, String deviceID){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userID", userID);
		params.put("deviceID", deviceID);
		List<UserDevice> userDevices = sqlSession.selectList("userVip.selectUserDeviceRole",params);
		if(userDevices == null || userDevices.size() == 0){
			return null;
		}else{
			return userDevices.get(0);
		}
	}
}
