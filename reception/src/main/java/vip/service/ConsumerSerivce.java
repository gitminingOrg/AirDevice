package vip.service;

import java.util.List;
import java.util.Map;

import model.consumer.ConsumerShareCode;
import model.vip.Consumer;
import utils.ResultData;
import vo.vip.ConsumerVo;

public interface ConsumerSerivce {
	ConsumerVo login(String phone, String password);
	
	ConsumerVo login(String wechat);
	
	List<ConsumerVo> fetch(Map<String, Object> condition);
	
	ConsumerVo create(Consumer consumer);
	
	ResultData fetchShareCode(Map<String, Object> condition);
	
	ResultData createShareCode(ConsumerShareCode code);
	
	boolean canShare(Map<String, Object> condition);
}
