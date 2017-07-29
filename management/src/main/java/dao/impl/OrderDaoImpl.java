package dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.OrderDao;
import model.order.TaobaoOrder;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao{
	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
	
	private Object lock = new Object();
	
	@Override
	public ResultData insert(List<TaobaoOrder> order) {
		ResultData result = new ResultData();
		for(TaobaoOrder item : order) {
			item.setOrderId(IDGenerator.generate("TBO"));
		}
		try {
			sqlSession.insert("management.externalorder.insertTaobaoBatch", order);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
