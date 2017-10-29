package finley.monitor.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finley.monitor.dao.CityPM25Dao;
import finley.monitor.service.CityPM25Service;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class CityPM25ServiceImpl implements CityPM25Service {
	private Logger logger = LoggerFactory.getLogger(CityPM25ServiceImpl.class);
	
	@Autowired
	private CityPM25Dao cityPM25Dao;
	
	@Override
	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = cityPM25Dao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		}else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

}
