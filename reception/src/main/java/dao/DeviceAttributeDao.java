package dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import bean.QRCode;

@Repository
public class DeviceAttributeDao extends BaseDaoImpl{
	public QRCode getQRCode(String codeValue){
		if(codeValue == null){
			return null;
		}
		List<QRCode> qrcodes = sqlSession.selectList("deviceAttribute.getOne", codeValue);
		if(qrcodes == null || qrcodes.size() == 0){
			return null;
		}else {
			return qrcodes.get(0);
		}
	}
	
	public boolean occupyQRCode(String codeValue){
		if(codeValue == null){
			return false;
		}else{
			return sqlSession.update("deviceAttribute.occupyQRCode", codeValue) >= 0;
		}
	}
	
	public boolean isDeviceAdvanced(String codeValue){
		List<Integer> result = sqlSession.selectList("deviceAttribute.QRAdvance", codeValue);
		if(result == null || result.size() == 0){
			return false;
		}
		for (Integer integer : result) {
			if(integer == 1){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getDeviceComponents(String deviceID){
		if(deviceID == null){
			return null;
		}else{
			return sqlSession.selectList("deviceAttribute.deviceComponent", deviceID);
		}
	}
	
	public int getDeviceVelocity(String deviceID){
		if(deviceID == null){
			return 500;
		}else{
			List<Integer> voList = sqlSession.selectList("deviceAttribute.deviceVelocity", deviceID);
			if(voList == null || voList.size() == 0){
				return 500;
			}
			return voList.get(0);
		}
	}
}
