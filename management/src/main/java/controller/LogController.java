package controller;

import com.alibaba.fastjson.JSON;
import form.SystemLogForm;
import form.UserLogForm;
import model.systemlog.SystemLog;
import model.userlog.UserLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.SystemLogService;
import service.UserLogService;
import utils.ResponseCode;
import utils.ResultData;

import javax.validation.Valid;
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
	public ResultData CreateUs(@Valid UserLogForm form, BindingResult br){
		ResultData result = new ResultData();
		if (br.hasErrors()) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("表单中含有非法参数");
			logger.error(JSON.toJSONString(br.getAllErrors()));
			return result;
		}
		UserLog userLog = new UserLog(form.getUserId(),form.getTarget(),form.getMessage());
		ResultData response= userLogService.create(userLog);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/us_update")
	public ResultData UpdateUs(UserLog userlog){
		ResultData result = new ResultData();
		ResultData response = userLogService.modify(userlog);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
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
	public ResultData CreateSy(@Valid SystemLogForm form, BindingResult br){
		ResultData result = new ResultData();
		if (br.hasErrors()) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("表单中含有非法参数");
			logger.error(JSON.toJSONString(br.getAllErrors()));
			return result;
		}
		SystemLog systemlog = new SystemLog(form.getTarget(),form.getMessage());
		ResultData response = systemLogService.create(systemlog);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		}
		else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/sy_update")
	public ResultData UpdateSy(SystemLog systemlog){
		ResultData result = new ResultData();
		ResultData response = systemLogService.modify(systemlog);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
			result.setData(response.getData());
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
