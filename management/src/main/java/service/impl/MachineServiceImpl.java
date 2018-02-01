package service.impl;

import dao.MachineDao;
import model.machine.IdleMachine;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pagination.DataTablePage;
import pagination.DataTableParam;
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
		ResultData latestMachineStatusResponse = machineDao.query(map);
		if (latestMachineStatusResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			resultData.setResponseCode(latestMachineStatusResponse.getResponseCode());
		} else if (latestMachineStatusResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			resultData.setData(machineResponse.getData());
		}

		List<MachineStatusVo> machineStatusVoList = (List<MachineStatusVo>) machineResponse.getData();
		List<MachineStataVo> latestMachineStatusList = (List<MachineStataVo>) latestMachineStatusResponse.getData();

		resultData.setData(getMachineStatus(machineStatusVoList, latestMachineStatusList));

		return resultData;
	}

	@Override
	public ResultData queryMachineStatus(Map<String, Object> condition, DataTableParam param) {
		ResultData resultData = new ResultData();
		ResultData machineResponse = machineDao.queryMachineStatus(condition, param);
		if (machineResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(machineResponse.getResponseCode());
			return resultData;
		}

		Map<String, Object> map = new HashMap<>();
		ResultData latestMachineStatusResponse = machineDao.query(map);
		if (latestMachineStatusResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			resultData.setResponseCode(latestMachineStatusResponse.getResponseCode());
		} else if (latestMachineStatusResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			resultData.setData(machineResponse.getData());
		}

		List<MachineStatusVo> machineStatusVoList = ((DataTablePage<MachineStatusVo>) machineResponse.getData()).getData();
		List<MachineStataVo> latestMachineStatusList = (List<MachineStataVo>) latestMachineStatusResponse.getData();


		DataTablePage<MachineStatusVo> page = (DataTablePage<MachineStatusVo>) machineResponse.getData();

		page.setData(getMachineStatus(machineStatusVoList, latestMachineStatusList));
		resultData.setData(page);

		return resultData;
	}


	private List<MachineStatusVo> getMachineStatus
			(List<MachineStatusVo> machineStatusVoList, List<MachineStataVo> latestMachineStatusList)
	{
		Map<String, MachineStatusVo> machineMap =
				machineStatusVoList.stream().collect(Collectors.toMap(e-> e.getChipId(), e -> e));
		Set<String> machineChips = machineMap.keySet();

		for (MachineStataVo latestMachineStatus : latestMachineStatusList) {
			if (machineChips.contains(latestMachineStatus.getMachineId())) {
				machineMap.get(latestMachineStatus.getMachineId()).setStatus(MachineStatus.ONLINE);
			} else {
//				MachineStatusVo machine = new MachineStatusVo();
//				machine.setChipId(latestMachineStatus.getMachineId());
//				machine.setDeviceId("未绑定");
//				machine.setModelCode("未知");
//				machine.setModelName("未知");
//				machine.setStatus(MachineStatus.IDLE);
//				machine.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
//				machineMap.put(machine.getChipId(), machine);
			}
		}
		List<MachineStatusVo> result = new LinkedList<>(machineMap.values());
		result.sort((e1, e2) -> e2.getUpdateTime().compareTo(e1.getUpdateTime()));

		return result;
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

	@Override
	public ResultData queryMachineStatusRange(Map<String, Object> condition) {
		return machineDao.queryMachineStatusRange(condition);
	}
}
