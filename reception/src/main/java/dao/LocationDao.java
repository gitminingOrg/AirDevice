package dao;

import model.location.City;
import model.location.District;
import model.location.Province;

public interface LocationDao {
	boolean insertProvince(Province province);
	
	boolean insertCity(City city);
	
	boolean insertDistrict(District district);
}
