package service;

import java.util.Map;

import utils.ResultData;

public interface ConsumerService {
	ResultData fetchConsumerGoods(Map<String, Object> condition);
}
