package service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ConsumerDao;
import pagination.DataTableParam;
import service.ConsumerService;
import utils.ResponseCode;
import utils.ResultData;
@Service
public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private ConsumerDao consumerDao;

	@Override
	public ResultData fetchConsumerGoods(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = consumerDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData fetchConsumerGoods(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = consumerDao.query(condition, param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

}
