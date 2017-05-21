package vip.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ConsumerDao;
import vip.service.ConsumerSerivce;
import vo.vip.ConsumerVo;

@Service
public class ConsumerServiceImpl implements ConsumerSerivce{
	private Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);
	
	@Autowired
	private ConsumerDao consumerDao;
	
	@Override
	public ConsumerVo login(String phone) {
		ConsumerVo result = new ConsumerVo();
		Map<String, Object> condition = new HashMap<>();
		condition.put("customerPhone", phone);
		List<ConsumerVo> list = consumerDao.query(condition);
		if(list.isEmpty()) {
			return null;
		}
		result = list.get(0);
		result.setCustomerId("uid3618");
		return result;
	}
	
	@Override
	public ConsumerVo fetch(Map<String, Object> condition) {
		ConsumerVo result = new ConsumerVo();
		result.setCustomerId("uid3618");
		return result;
	}
	
}
