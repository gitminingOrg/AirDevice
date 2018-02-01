package dao;

import java.util.Map;

import model.machine.IdleMachine;
import pagination.DataTableParam;
import utils.ResultData;

public interface MachineDao {

	ResultData deleteDevice(String deviceId);

	ResultData insertIdleMachine(IdleMachine machine);

	ResultData queryIdleMachine(Map<String, Object> condition);

	ResultData updateIdleMachine(Map<String, Object> condition);

	ResultData queryMachineStatus(Map<String, Object> condition);

	ResultData queryMachineStatus(Map<String, Object> condition, DataTableParam param);

	ResultData query(Map<String, Object> condition);

	ResultData updateidle(Map<String, Object> condition);

	ResultData queryEverydayRecord(Map<String, Object> condition);

	ResultData queryMachineStatusRange(Map<String, Object> condition);
}
