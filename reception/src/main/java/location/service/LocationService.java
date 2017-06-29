package location.service;

import java.util.List;
import java.util.Map;

import vo.location.DeviceCityVo;

public interface LocationService {
	String locateByIp(String ip);

	void init();
	
	List<DeviceCityVo> fetch(Map<String, Object> condition);
	
	DeviceCityVo getDeviceLocation(String deviceId);
}
