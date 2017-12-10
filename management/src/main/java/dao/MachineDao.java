package dao;

import java.util.Map;

import model.machine.IdleMachine;
import utils.ResultData;

public interface MachineDao {

	ResultData deleteDevice(String deviceId);

	ResultData insertIdleMachine(IdleMachine machine);

	ResultData queryIdleMachine(Map<String, Object> condition);

	ResultData updateIdleMachine(Map<String, Object> condition);

	ResultData queryMachineStatus(Map<String, Object> condition);

	ResultData query(Map<String, Object> condition);
}
