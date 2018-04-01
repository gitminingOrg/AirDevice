package finley.monitor.service;

import java.util.Map;

import utils.ResultData;

public interface CityPM25Service {
	ResultData fetch(Map<String, Object> condition);

	ResultData fetchDeviceCity(Map<String, Object> condition);
}
