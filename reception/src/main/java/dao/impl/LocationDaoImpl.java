package dao.impl;

import java.util.List;
import java.util.Map;

import model.location.City;
import model.location.District;
import model.location.Province;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import vo.location.DeviceCityVo;
import dao.BaseDaoImpl;
import dao.LocationDao;

@Repository
public class LocationDaoImpl extends BaseDaoImpl implements LocationDao {
	private Logger logger = LoggerFactory.getLogger(LocationDaoImpl.class);

	@Override
	public boolean insertProvince(Province province) {
		try {
			sqlSession.insert("province.insert", province);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insertCity(City city) {
		try {
			sqlSession.insert("city.insert", city);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insertDistrict(District district) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DeviceCityVo> query4Device(Map<String, Object> condition) {
		try {
			List<DeviceCityVo> list = sqlSession.selectList("city.query4device", condition);
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Province> getAllProvince() {
		try {
			List<Province> list = sqlSession.selectList("province.selectAll");
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<City> getAllProvinceCity(String provinceID) {
		try {
			List<City> list = sqlSession.selectList("city.cityOfProvince", provinceID);
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public City getCityByID(String cityID) {
		try {
			List<City> list = sqlSession.selectList("city.cityById", cityID);
			if(list == null || list.size() == 0){
				return null;
			}else{
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Province getProvinceByID(String provinceID) {
		try {
			List<Province> list = sqlSession.selectList("province.provinceByID", provinceID);
			if(list == null || list.size() == 0){
				return null;
			}else{
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
