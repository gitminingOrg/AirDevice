package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.ConsumerDao;
import model.vip.Consumer;
import utils.IDGenerator;
import vo.vip.ConsumerVo;

@Repository
public class ConsumerDaoImpl extends BaseDaoImpl implements ConsumerDao {
	private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);

	private Object lock = new Object();

	@Override
	public List<ConsumerVo> query(Map<String, Object> condition) {
		List<ConsumerVo> result = new ArrayList<>();
		try {
			result = sqlSession.selectList("consumer.query", condition);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public boolean insert(Consumer consumer) {
		consumer.setConsumerId(IDGenerator.generate("CON"));
		synchronized (lock) {
			try {
				sqlSession.insert("consumer.insert", consumer);
				return true;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}

}
