package dao;

import java.util.List;
import java.util.Map;

import model.location.City;
import model.location.District;
import model.location.Province;
import vo.location.DeviceCityVo;

public interface LocationDao {
	boolean insertProvince(Province province);
	
	boolean insertCity(City city);
	
	boolean insertDistrict(District district);
	
	List<DeviceCityVo> query4Device(Map<String, Object> condition);
	
	List<Province> getAllProvince();
	
	List<City> getAllProvinceCity(String provinceID);
	
	City getCityByID(String cityID);
	
	Province getProvinceByID(String provinceID);
}
