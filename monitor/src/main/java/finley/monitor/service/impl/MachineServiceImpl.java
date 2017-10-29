package finley.monitor.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finley.monitor.dao.MachineDao;
import finley.monitor.service.MachineService;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class MachineServiceImpl implements MachineService{
	private Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);
	
	@Autowired
	private MachineDao machineDao;
	
	@Override
	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = machineDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		}else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

}
