package dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.DeviceChipDao;
import model.device.DeviceChip;
import utils.IDGenerator;

@Repository
public class DeviceChipDaoImpl extends BaseDaoImpl implements DeviceChipDao{
	private Logger logger = LoggerFactory.getLogger(DeviceChipDaoImpl.class);

	@Override
	public boolean insert(DeviceChip dc) {
		dc.setBindId(IDGenerator.generate("DEC"));
		try {
			sqlSession.insert("device_chip.insert", dc);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
