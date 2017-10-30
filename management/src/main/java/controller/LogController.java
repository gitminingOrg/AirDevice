package controller;

import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.UserLogService;
import utils.ResponseCode;
import utils.ResultData;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {
	private Logger logger = LoggerFactory.getLogger(LogController.class);

	private UserLogService userLogService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public ResultData log4user() {
		ResultData result = new ResultData();

		Map<String, Object> condition = new HashMap<>();
		//Map.put()?

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
	
}
