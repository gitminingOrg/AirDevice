package vip.service;

import java.util.Map;

import vo.vip.ConsumerVo;

public interface ConsumerSerivce {
	ConsumerVo login(String phone);
	
	ConsumerVo fetch(Map<String, Object> condition);
}
