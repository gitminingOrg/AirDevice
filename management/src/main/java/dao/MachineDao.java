package dao;

import java.util.Map;

import model.machine.IdleMachine;
import utils.ResultData;

public interface MachineDao {

	public int deleteDevice(String deviceId);

	public int releaseQrCode(String QrCode);

	ResultData insertIdleMachine(IdleMachine machine);

	ResultData queryIdleMachine(Map<String, Object> condition);
}
