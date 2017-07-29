package dao;

import java.util.Map;

import model.consumer.ConsumerShareCode;
import utils.ResultData;

public interface ConsumerShareCodeDao {
	ResultData insert(ConsumerShareCode code);
	
	ResultData query(Map<String, Object> condition);
}
