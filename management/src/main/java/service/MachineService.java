package service;

import java.util.Map;

import model.machine.IdleMachine;
import utils.ResultData;

public interface MachineService {

    public int deleteDevice(String deviceId);
    
    ResultData createIdleMachine(IdleMachine machine);
    
    ResultData fetchIdleMachine(Map<String, Object> condition);
}
