package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerGoods;
import dao.BaseDao;
import dao.ConsumerDao;
@Repository
public class ConsumerDaoImpl extends BaseDao implements ConsumerDao {
	private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);
	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<ConsumerGoods> list = sqlSession.selectList("management.consumer.consumerGoods.query", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

}
