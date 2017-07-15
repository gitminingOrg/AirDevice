package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.ComponentDao;
import model.goods.Component;
import pagination.DataTablePage;
import pagination.DataTableParam;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.goods.GoodsComponentVo;

@Repository
public class ComponentDaoImpl extends BaseDao implements ComponentDao {
	private Logger logger = LoggerFactory.getLogger(ComponentDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<GoodsComponentVo> list = sqlSession.selectList("management.goods.component.query", condition);
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
	public ResultData query(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<GoodsComponentVo> page = new DataTablePage<>(param);
		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<GoodsComponentVo> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<GoodsComponentVo> queryByPage(Map<String, Object> condition, int start, int length) {
		List<GoodsComponentVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.goods.component.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public ResultData insert(Component component) {
		ResultData result = new ResultData();
		component.setComponentId(IDGenerator.generate("COL"));
		synchronized (lock) {
			try {
				sqlSession.insert("management.goods.component.insert", component);
				result.setData(component);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}
}
