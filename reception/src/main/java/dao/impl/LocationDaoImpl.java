package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.LocationDao;
import model.location.City;
import model.location.District;
import model.location.Province;
import vo.location.DeviceCityVo;

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
}
