package service;

import java.util.List;
import java.util.Map;

import model.machine.IdleMachine;
import utils.ResultData;

public interface MachineService {

    ResultData deleteDevice(String deviceId);
    
    ResultData createIdleMachine(IdleMachine machine);
    
    ResultData fetchIdleMachine(Map<String, Object> condition);

    ResultData updateIdleMachine(Map<String, Object> condition);

    ResultData queryMachineStatus(Map<String, Object> condition);

    ResultData queryMachineStata(Map<String, Object> condition);

    ResultData mdifyidle(Map<String, Object> condition);

    ResultData fetchRecord(Map<String, Object> condtion);
}
