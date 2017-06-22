package dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.LocationDao;
import model.location.City;
import model.location.District;
import model.location.Province;

@Repository
public class LocationDaoImpl extends BaseDaoImpl implements LocationDao{
	private Logger logger = LoggerFactory.getLogger(LocationDaoImpl.class);
	
	@Override
	public boolean insertProvince(Province province) {
		try {
			sqlSession.insert("province.insert", province);
			return true;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insertCity(City city) {
		try {
			sqlSession.insert("city.insert", city);
			return true;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insertDistrict(District district) {
		// TODO Auto-generated method stub
		return false;
	}
}
