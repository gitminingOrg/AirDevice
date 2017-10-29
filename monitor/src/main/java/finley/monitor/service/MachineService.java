package finley.monitor.service;

import java.util.Map;

import utils.ResultData;

public interface MachineService {
	ResultData fetch(Map<String, Object> condition);
}
