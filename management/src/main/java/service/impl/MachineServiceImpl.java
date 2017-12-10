package service.impl;

import dao.MachineDao;
import model.machine.IdleMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MachineService;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.MachineStatusVo;

@Service
public class MachineServiceImpl implements MachineService {
	@Autowired
	private MachineDao machineDao;

	@Override
	public ResultData deleteDevice(String deviceId) {
		// 先删除相关的设备操作与绑定记录，在更新二维码的占有状态
		ResultData resultData = machineDao.deleteDevice(deviceId);

		return resultData;
	}

	@Override
	public ResultData createIdleMachine(IdleMachine machine) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("uid", machine.getUid());
		ResultData response = machineDao.queryIdleMachine(condition);
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			result.setDescription("该设备已被录入，无需再次录入");
			return result;
		}
		response = machineDao.insertIdleMachine(machine);
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

	@Override
	public ResultData updateIdleMachine(Map<String, Object> condition) {
		return machineDao.updateIdleMachine(condition);
	}

	@Override
	public ResultData queryMachineStatus(Map<String, Object> condition, List<String> chips) {
		ResultData response = machineDao.queryMachineStatus(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK || chips.size() == 0) {
			return response;
		} else {
			ResultData resultData = new ResultData();
			List<MachineStatusVo> list = (List<MachineStatusVo>) response.getData();
			for (MachineStatusVo machineStatusVo : list) {
				if (chips.contains(machineStatusVo.getChipId())) {
					machineStatusVo.setStatus(1);
				}
			}
			resultData.setData(list);
			return resultData;
		}
	}

    @Override
    public ResultData queryMachineStata(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
