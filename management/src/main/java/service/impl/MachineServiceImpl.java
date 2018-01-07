package service.impl;

import dao.MachineDao;
import model.machine.IdleMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MachineService;
import utils.MachineStatus;
import utils.ResponseCode;
import utils.ResultData;
import vo.MachineStataVo;
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
	public ResultData queryMachineStatus(Map<String, Object> condition) {
		ResultData resultData = new ResultData();
		ResultData machineResponse = machineDao.queryMachineStatus(condition);
		if (machineResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(machineResponse.getResponseCode());
			return resultData;
		}

		Map<String, Object> map = new HashMap<>();
		ResultData latestMachineStatusReponse = machineDao.query(map);
		if (latestMachineStatusReponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			resultData.setResponseCode(latestMachineStatusReponse.getResponseCode());
		} else if (latestMachineStatusReponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			resultData.setData(machineResponse.getData());
		}

		List<MachineStatusVo> machineStatusVoList = (List<MachineStatusVo>) machineResponse.getData();

		Map<String, MachineStatusVo> machineMap =
				machineStatusVoList.stream().collect(Collectors.toMap(e-> e.getChipId(), e -> e));
		Set<String> machineChips = machineMap.keySet();
		List<MachineStataVo> latestMachineStatusList = (List<MachineStataVo>) latestMachineStatusReponse.getData();

		for (MachineStataVo latestMachineStatus : latestMachineStatusList) {
			if (machineChips.contains(latestMachineStatus.getMachineId())) {
				machineMap.get(latestMachineStatus.getMachineId()).setStatus(MachineStatus.ONLINE);
			} else {
				MachineStatusVo machine = new MachineStatusVo();
				machine.setChipId(latestMachineStatus.getMachineId());
				machine.setStatus(MachineStatus.IDLE);
				machineMap.put(machine.getChipId(), machine);
			}
		}

		resultData.setData(machineMap.values());

		return resultData;
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

    @Override
    public ResultData mdifyidle(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineDao.updateidle(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	result.setData(response.getData());
		} else {
        	result.setDescription(response.getDescription());
		}
		return result;
    }

    @Override
    public ResultData fetchRecord(Map<String, Object> condtion) {
        ResultData result = new ResultData();
        ResultData response = machineDao.queryEverydayRecord(condtion);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
        	result.setData(response.getData());
		} else {
        	result.setDescription(response.getDescription());
		}
		return result;
    }
}
