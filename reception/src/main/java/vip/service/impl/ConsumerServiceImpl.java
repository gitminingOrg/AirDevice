package vip.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dao.ConsumerDao;
import model.vip.Consumer;
import vip.service.ConsumerSerivce;
import vo.vip.ConsumerVo;

@Service
public class ConsumerServiceImpl implements ConsumerSerivce {
	private Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);

	@Autowired
	private ConsumerDao consumerDao;

	@Override
	public ConsumerVo login(String phone, String password) {
		Map<String, Object> condition = new HashMap<>();
		condition.put("customerPhone", phone);
		List<ConsumerVo> list = consumerDao.query(condition);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public ConsumerVo login(String wechat) {
		Map<String, Object> condition = new HashMap<>();
		condition.put("wechat", wechat);
		List<ConsumerVo> list = consumerDao.query(condition);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<ConsumerVo> fetch(Map<String, Object> condition) {
		List<ConsumerVo> list = consumerDao.query(condition);
		if (list.isEmpty()) {
			return null;
		}
		return list;
	}

	@Override
	public ConsumerVo create(Consumer consumer) {
		boolean status = consumerDao.insert(consumer);
		if (status) {
			Map<String, Object> condition = new HashMap<>();
			if (!StringUtils.isEmpty(consumer.getWechat())) {
				condition.put("wechat", consumer.getWechat());
			}
			return fetch(condition).get(0);
		}
		return null;
	}
}
