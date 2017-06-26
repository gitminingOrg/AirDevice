package vip.service;

import java.util.List;
import java.util.Map;

import model.vip.Consumer;
import vo.vip.ConsumerVo;

public interface ConsumerSerivce {
	ConsumerVo login(String phone, String password);
	
	ConsumerVo login(String wechat);
	
	List<ConsumerVo> fetch(Map<String, Object> condition);
	
	ConsumerVo create(Consumer consumer);
}
