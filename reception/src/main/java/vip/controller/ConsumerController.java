package vip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import form.LoginForm;
import model.ResultMap;
import model.consumer.ConsumerShareCode;
import utils.ResponseCode;
import utils.ResultData;
import vip.service.ConsumerSerivce;
import vo.consumer.ConsumerShareCodeVo;
import vo.vip.ConsumerVo;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
	private Logger logger = LoggerFactory.getLogger(ConsumerController.class);

	@Autowired
	private ConsumerSerivce consumerService;
	
	@RequiresGuest
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public ResultMap login(LoginForm form) {
		ResultMap result = new ResultMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(form.getPhone(), ""));
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo(e.getMessage());
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/info")
	public ResultMap info() {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("consumer", current);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/share/reference")
	public ResultMap shareCode() {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		String customerId = current.getCustomerId();
		Map<String, Object> condition = new HashMap<>();
		condition.put("userId", customerId);
		condition.put("status", 1);
		boolean status = consumerService.canShare(condition);
		if(!status) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("Do not have permission to share.");
			return result;
		}
		condition.clear();
		condition.put("consumerId", customerId);
		condition.put("blockFlag", false);
		ResultData response = consumerService.fetchShareCode(condition);
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			ConsumerShareCodeVo vo = ((List<ConsumerShareCodeVo>) response.getData()).get(0);
			result.setStatus(ResultMap.STATUS_SUCCESS);
			result.addContent("sharecode", vo);
			return result;
		}
		ConsumerShareCode code = new ConsumerShareCode(customerId);
		response = consumerService.createShareCode(code);
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setStatus(ResultMap.STATUS_SUCCESS);
			result.addContent("sharecode", response.getData());
			return result;
		}
		result.setStatus(ResultMap.STATUS_FAILURE);
		return result;
	}
}
