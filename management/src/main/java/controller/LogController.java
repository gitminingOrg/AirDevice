package controller;

import model.systemlog.SystemLog;
import model.userlog.UserLog;
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

	@RequestMapping(method = RequestMethod.GET, value = "/us_list")
	public ResultData log4user() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
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

	@RequestMapping(method = RequestMethod.POST, value = "/us_insert")
	public ResultData CreateUs(UserLog userLog){
		ResultData result = new ResultData();
		ResultData response= userLogService.create(userLog);
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

	@RequestMapping(method = RequestMethod.POST, value = "/us_update")
	public ResultData UpdateUs(UserLog userlog){
		ResultData result = new ResultData();
		ResultData response = userLogService.modify(userlog);
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

	@RequestMapping(method = RequestMethod.GET, value = "/sy_list")
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

	@RequestMapping(method = RequestMethod.POST, value = "/sy_insert")
	public ResultData CreateSy(SystemLog systemlog){
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

	@RequestMapping(method = RequestMethod.POST, value = "/sy_update")
	public ResultData UpdateSy(SystemLog systemlog){
		ResultData result = new ResultData();
		ResultData response = systemLogService.modify(systemlog);
		try {
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
				result.setData(response.getData());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
