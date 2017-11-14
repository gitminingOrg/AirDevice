package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import dao.BaseDao;
import dao.OrderDao;
import model.order.CustomizeOrder;
import model.order.TaobaoOrder;
import pagination.DataTablePage;
import pagination.DataTableParam;
import sun.font.EAttribute;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;
import vo.order.OrderVo;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {
	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insert(List<TaobaoOrder> order) {
		ResultData result = new ResultData();
		for (TaobaoOrder item : order) {
			item.setOrderId(IDGenerator.generate("TBO"));
		}
		try {
			sqlSession.insert("management.externalorder.insertTaobaoBatch", order);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<OrderVo> list = sqlSession.selectList("management.externalorder.query", condition);
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
		DataTablePage<OrderVo> page = new DataTablePage<>(param);
		if (!StringUtils.isEmpty(param.getsSearch())) {
			condition.put("search", new StringBuffer("%").append(param.getsSearch()).append("%").toString());
		}
		JSONObject params = JSON.parseObject(param.getParams());
		if(!StringUtils.isEmpty(params)) {
			if(!StringUtils.isEmpty(params.get("channel"))) {
				condition.put("channel", params.getString("channel"));
			}
			if(!StringUtils.isEmpty(params.get("status"))) {
				condition.put("status", params.getString("status"));
			}
		}
		ResultData total = query(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List) total.getData()).size());
		page.setiTotalDisplayRecords(((List) total.getData()).size());
		List<OrderVo> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<OrderVo> queryByPage(Map<String, Object> condition, int start, int length) {
		List<OrderVo> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.externalorder.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public ResultData update(TaobaoOrder order) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("management.externalorder.update", order);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData blockOrder(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("management.externalorder.blockOrder", condition);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData create(CustomizeOrder order) {
		ResultData result = new ResultData();
		order.setOrderId(IDGenerator.generate("CSO"));
		try {
			sqlSession.insert("management.customizeorder.insert", order);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData channel() {
		ResultData result = new ResultData();
		try {
			List<String> list = sqlSession.selectList("management.externalorder.channel");
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData status() {
		ResultData result = new ResultData();
		try {
			List<String> list = sqlSession.selectList("management.externalorder.status");
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
