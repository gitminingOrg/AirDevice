package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.GoodsDao;
import vo.goods.ConsumerGoodsVo;

@Repository
public class GoodsDaoImpl extends BaseDaoImpl implements GoodsDao {
	private Logger logger = LoggerFactory.getLogger(GoodsDaoImpl.class);

	@Override
	public List<ConsumerGoodsVo> query4Consumer(Map<String, Object> condition) {
		List<ConsumerGoodsVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("goods.query4consumer", condition);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

}
