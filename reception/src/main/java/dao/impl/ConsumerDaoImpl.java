package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.ConsumerDao;
import vo.vip.ConsumerVo;

@Repository
public class ConsumerDaoImpl extends BaseDaoImpl implements ConsumerDao{
	private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);
	
	@Override
	public List<ConsumerVo> query(Map<String, Object> condition) {
		List<ConsumerVo> result = new ArrayList<>();
		try {
			result = sqlSession.selectList("consumer.query", condition);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
