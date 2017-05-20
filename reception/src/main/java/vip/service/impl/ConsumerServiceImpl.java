package vip.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vip.service.ConsumerSerivce;
import vo.vip.ConsumerVo;

@Service
public class ConsumerServiceImpl implements ConsumerSerivce{
	private Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);
	
	@Override
	public ConsumerVo login(String phone) {
		ConsumerVo result = new ConsumerVo();
		result.setCustomerID("uid3618");
		return result;
	}
	
	@Override
	public ConsumerVo fetch(Map<String, Object> condition) {
		ConsumerVo result = new ConsumerVo();
		result.setCustomerID("uid3618");
		return result;
	}
	
}
