package dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import bean.DeviceName;
import bean.DeviceStatus;

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
}
