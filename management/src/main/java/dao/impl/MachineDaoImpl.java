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
import vo.machine.MachineStatusVo;

@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao {
	private Logger logger = LoggerFactory.getLogger(MachineDaoImpl.class);

	@Override
	public ResultData deleteDevice(String deviceId) {
		ResultData result = new ResultData();
		try {
			sqlSession.delete("management.machine.deleteDevice", deviceId);
			sqlSession.delete("management.machine.releaseQrcode", deviceId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return result;
		}
		return result;
	}

	@Override
	public ResultData insertIdleMachine(IdleMachine machine) {
		ResultData result = new ResultData();
		machine.setImId(IDGenerator.generate("IME"));
		try {
			sqlSession.insert("management.machine.idle.insert", machine);
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

	@Override
	public ResultData updateIdleMachine(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("management.machine.idle.update", condition);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData queryMachineStatus(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<MachineStatusVo> list = sqlSession.selectList("management.machine.queryMachineStatus", condition);
			if (list.size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
				return result;
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
