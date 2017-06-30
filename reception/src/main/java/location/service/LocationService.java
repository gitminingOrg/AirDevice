package location.service;

import java.util.List;
import java.util.Map;

import model.location.City;
import model.location.Province;
import vo.location.DeviceCityVo;

public interface LocationService {
	String locateByIp(String ip);

	void init();
	
	List<DeviceCityVo> fetch(Map<String, Object> condition);
	
	DeviceCityVo getDeviceLocation(String deviceId);
	
	List<Province> getAllProvinces();
	
	List<City> getProvinceCity(Province province);
	
	City getCityByID(String cityID);
	
	Province getProvinceByID(String provinceID);
}
