package service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.OrderDao;
import model.order.TaobaoOrder;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class OrderServiceImpl implements OrderService{
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public ResultData upload(List<TaobaoOrder> order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.insert(order);
		result.setResponseCode(response.getResponseCode());
		if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

}
