package service;

import java.util.Map;

import pagination.DataTableParam;
import utils.ResultData;

public interface ConsumerService {
	ResultData fetchConsumerGoods(Map<String, Object> condition);
	
	ResultData fetchConsumerGoods(Map<String, Object> condition, DataTableParam param);
	
	ResultData fetchShareCode(Map<String, Object> condition, DataTableParam param);

	ResultData fetchConsumerMachines(Map<String, Object> condition);
}
