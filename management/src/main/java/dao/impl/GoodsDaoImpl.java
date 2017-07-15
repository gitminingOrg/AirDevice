package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.GoodsDao;
import utils.ResponseCode;
import utils.ResultData;
import vo.goods.GoodsVo;

@Repository
public class GoodsDaoImpl extends BaseDao implements GoodsDao {
	private Logger logger = LoggerFactory.getLogger(GoodsDaoImpl.class);

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<GoodsVo> list = sqlSession.selectList("management.goods.query", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
