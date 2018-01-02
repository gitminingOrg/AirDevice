package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.order.GuoMaiOrder;
import model.order.OrderCommodity;
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
import vo.order.GuoMaiOrderVo;
import vo.order.OrderStatusVo;
import vo.order.OrderVo;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {
	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insert(List<GuoMaiOrder> order) {
		ResultData result = new ResultData();
		for (GuoMaiOrder item : order) {
			item.setOrderId(IDGenerator.generate("TBO"));
		}
		try {
			sqlSession.insert("management.guomaiorder.insertBatch", order);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData insertCommodity(List<OrderCommodity> commodityList) {
		ResultData result = new ResultData();
		for (OrderCommodity item : commodityList) {
			item.setCommodityId(IDGenerator.generate("GMC"));
		}
		try {
			sqlSession.insert("management.ordercommodity.insertBatch", commodityList);
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
			List<GuoMaiOrderVo> list = sqlSession.selectList("management.guomaiorder.query", condition);
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
		DataTablePage<GuoMaiOrder> page = new DataTablePage<>(param);
		if (!StringUtils.isEmpty(param.getsSearch())) {
			condition.put("search", new StringBuffer("%").append(param.getsSearch()).append("%").toString());
		}
		JSONObject params = JSON.parseObject(param.getParams());
		if(!StringUtils.isEmpty(params)) {
			if(!StringUtils.isEmpty(params.get("channel"))) {
				condition.put("orderChannel", params.getString("channel"));
			}
			if(!StringUtils.isEmpty(params.get("status"))) {
				condition.put("orderStatus", params.getString("status"));
			}
			if (!StringUtils.isEmpty(params.get("startDate"))) {
				condition.put("startTime", params.getString("startDate"));
			}
			if (!StringUtils.isEmpty(params.get("endDate"))) {
				condition.put("endTime", params.getString("endDate"));
			}
			if (!StringUtils.isEmpty(params.get("province"))) {
				condition.put("province", params.getString("province"));
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
		List<GuoMaiOrder> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<GuoMaiOrder> queryByPage(Map<String, Object> condition, int start, int length) {
		List<GuoMaiOrder> list = new ArrayList<>();
		try {
			list = sqlSession.selectList("management.guomaiorder.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public ResultData update(GuoMaiOrder order) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("management.guomaiorder.update", order);
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
	public ResultData create(GuoMaiOrder order) {
		ResultData result = new ResultData();
		order.setOrderId(IDGenerator.generate("GMO"));
		try {
			sqlSession.insert("management.guomaiorder.insert", order);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData create(OrderCommodity commodity) {
		ResultData result = new ResultData();
		commodity.setCommodityId(IDGenerator.generate("GMC"));
		try {
			sqlSession.insert("management.ordercommodity.insert", commodity);
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

	@Override
	public ResultData queryOrderStatus(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<OrderStatusVo> list = sqlSession.selectList("management.orderstatus.queryOrder", condition);
			if (list.size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
				return result;
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
	public ResultData updateBatchCommodity(List<OrderCommodity> commodityList) {
		ResultData result = new ResultData();
		try {
			int rows = sqlSession.update("management.ordercommodity.updateBatch", commodityList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}

		return result;
	}

	@Override
	public ResultData blockCommodity(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			int rows = sqlSession.update("management.ordercommodity.blockcommodity", condition);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
