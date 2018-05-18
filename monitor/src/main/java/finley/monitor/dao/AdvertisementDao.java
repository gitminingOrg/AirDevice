package finley.monitor.dao;

import utils.ResultData;

import java.util.Map;

public interface AdvertisementDao {

    ResultData query(Map<String, Object> condition);
}
