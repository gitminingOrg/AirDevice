package dao.impl;

import dao.BaseDao;
import dao.GoodsModelDao;
import model.goods.GoodsModel;
import model.goods.ModelComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.goods.GoodsModelVo;
import vo.goods.ModelDetailVo;

import java.util.List;
import java.util.Map;

@Repository
public class GoodsModelDaoImpl extends BaseDao implements GoodsModelDao {
	private Logger logger = LoggerFactory.getLogger(GoodsModelDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<GoodsModelVo> list = sqlSession.selectList("management.goods.model.query", condition);
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

	@Override
	public ResultData queryModelDetail(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<ModelDetailVo> list = sqlSession.selectList("management.goods.modelcomponent.query", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getLocalizedMessage());
		}
		return result;
	}

	@Override
	public ResultData insert(GoodsModel model) {
		ResultData result = new ResultData();
		model.setModelId(IDGenerator.generate("MOD"));
		synchronized (lock) {
			try {
				sqlSession.insert("management.goods.model.insert", model);
				result.setData(model);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}

	@Override
	public ResultData insertComponent4Model(List<ModelComponent> list) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.insert("management.goods.modelcomponent.insertList", list);
				result.setData(list);
			}catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}

}
