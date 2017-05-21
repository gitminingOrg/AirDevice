package vip.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import form.LoginForm;
import model.ResultMap;
import vo.vip.ConsumerVo;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
	private Logger logger = LoggerFactory.getLogger(ConsumerController.class);
	
	@RequiresGuest
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public ResultMap login(LoginForm form) {
		ResultMap result = new ResultMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(form.getPhone(), ""));
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo(e.getMessage());
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		return result;
	}
	
	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value="/info")
	public ResultMap info() {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo)subject.getPrincipal();
		if(StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("consumer", current);
		return result;
	}
}
