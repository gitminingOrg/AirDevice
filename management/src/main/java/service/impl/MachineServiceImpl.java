package service.impl;

import dao.MachineDao;
import model.machine.IdleMachine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MachineService;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class MachineServiceImpl implements MachineService {
	@Autowired
	private MachineDao machineDao;

	@Override
	public int deleteDevice(String deviceId) {
		// 先删除相关的设备操作与绑定记录，在更新二维码的占有状态
		int delete = machineDao.deleteDevice(deviceId);
		int update = machineDao.releaseQrCode(deviceId);

		// System.out.println("delete: " + delete);
		// System.out.println("update: " + update);

		return update;
	}

	@Override
	public ResultData createIdleMachine(IdleMachine machine) {
		ResultData result = new ResultData();
		ResultData response = machineDao.insertIdleMachine(machine);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchIdleMachine(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = machineDao.queryIdleMachine(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
