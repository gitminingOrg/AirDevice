package dao.impl;

import dao.BaseDao;
import dao.MachineDao;
import model.machine.IdleMachine;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.IdleMachineVo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao {
	private Logger logger = LoggerFactory.getLogger(MachineDaoImpl.class);

	@Override
	public int deleteDevice(String deviceId) {
		return sqlSession.delete("management.machine.deleteDevice", deviceId);
	}

	@Override
	public int releaseQrCode(String QrCode) {
		return sqlSession.update("management.machine.releaseQrcode", QrCode);
	}

	@Override
	public ResultData insertIdleMachine(IdleMachine machine) {
		ResultData result = new ResultData();
		machine.setImId(IDGenerator.generate("IME"));
		try {
			sqlSession.insert("management.machine.idle.insert");
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData queryIdleMachine(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<IdleMachineVo> list = sqlSession.selectList("management.machine.idle.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
