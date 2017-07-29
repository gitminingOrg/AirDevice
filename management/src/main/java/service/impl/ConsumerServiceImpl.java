package service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ConsumerDao;
import dao.ShareCodeDao;
import pagination.DataTableParam;
import service.ConsumerService;
import utils.ResponseCode;
import utils.ResultData;
@Service
public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private ConsumerDao consumerDao;
	
	@Autowired
	private ShareCodeDao shareCodeDao;

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

	@Override
	public ResultData fetchShareCode(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = shareCodeDao.query(condition, param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
