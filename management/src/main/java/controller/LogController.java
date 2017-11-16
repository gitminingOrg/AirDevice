package controller;

import model.systemlog.SystemLog;
import model.userlog.UserLog;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.SystemLogService;
import service.UserLogService;
import utils.ResponseCode;
import utils.ResultData;

import java.util.HashMap;
import java.util.Map;
import java.lang.String;

@RestController
@RequestMapping("/log")
public class LogController {
	private Logger logger = LoggerFactory.getLogger(LogController.class);

	@Autowired
	private UserLogService userLogService;
	@Autowired
	private SystemLogService systemLogService;

	@RequestMapping(method = RequestMethod.GET, value = "/userlist")
	public ResultData log4user(String userId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		condition.put("userId", userId);
		ResultData response = userLogService.fetch(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		}
		else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/useradd")
	public ResultData create(UserLog userLog){
		ResultData result = new ResultData();
		ResultData response= userLogService.createUserLog(userLog);
		result.setResponseCode(response.getResponseCode());
		try {
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
				result.setData(response.getData());
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	public ResultData Update(UserLog userlog){
		ResultData result = new ResultData();
		ResultData response = userLogService.UpdateLog(userlog);
		try {
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
				result.setData(response.getData());
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/systemlist")
	public ResultData log4system(){
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData response = systemLogService.fetch(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/systemadd")
	public ResultData createSystem(SystemLog systemlog){
		ResultData result = new ResultData();
		ResultData response = systemLogService.create(systemlog);
		result.setResponseCode(response.getResponseCode());
		try {
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
				result.setData(response.getData());
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
