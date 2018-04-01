package finley.monitor.dao;

import java.util.Map;

import utils.ResultData;

public interface CityPM25Dao {
	ResultData query(Map<String, Object> condition);

	ResultData queryDeviceCity(Map<String, Object> condition);
}
