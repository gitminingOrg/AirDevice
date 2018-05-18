package finley.monitor.service;

import utils.ResultData;

import java.util.Map;

public interface AdvertisementService {

    ResultData fetch(Map<String, Object> condition);
}
