package vip.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import form.LoginForm;
import model.ResultMap;

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
}
