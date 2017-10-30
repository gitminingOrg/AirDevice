package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Class;

import service.UserLogService;
import utils.ResultData;

@RestController
@RequestMapping("/log")
public class LogController {
	private Logger logger = LoggerFactory.getLogger(LogController.class);
	
	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public ResultData log4user() {
		ResultData result = new ResultData();
		ResultData response = UserLogService.
		return result;
	}
	
}
