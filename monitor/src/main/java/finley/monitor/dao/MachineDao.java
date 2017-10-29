package finley.monitor.dao;

import java.util.Map;

import utils.ResultData;

public interface MachineDao {
	ResultData query(Map<String, Object> condition);
}
