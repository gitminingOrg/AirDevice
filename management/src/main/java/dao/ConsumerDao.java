package dao;

import java.util.Map;

import utils.ResultData;

public interface ConsumerDao {
	ResultData query(Map<String, Object> condition);
}
