package service.impl;

import java.util.List;
import java.util.Map;

import dao.OrderBatchDao;
import dao.OrderChannelDao;
import model.order.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.OrderDao;
import dao.OrderMissionDao;
import pagination.DataTableParam;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.OrderChannelVo;
import vo.order.OrderStatusVo;
import vo.order.OrderVo;

@Service
public class OrderServiceImpl implements OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderMissionDao orderMissionDao;

	@Autowired
	private OrderChannelDao orderChannelDao;

	@Autowired
	private OrderBatchDao orderBatchDao;

	@Override
	public ResultData upload(List<TaobaoOrder> order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.insert(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetch(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = orderDao.query(condition, param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData assign(TaobaoOrder order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.update(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData blockOrder(Map<String, Object> condition) {
		return orderDao.blockOrder(condition);
	}

	@Override
	public ResultData create(CustomizeOrder order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.create(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	public ResultData create(OrderMission mission) {
		ResultData result = new ResultData();
		ResultData response = orderMissionDao.insert(mission);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchMission4Order(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderMissionDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchChannel() {
		ResultData result = new ResultData();
		ResultData response = orderDao.channel();
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchStatus() {
		ResultData result = new ResultData();
		ResultData response = orderDao.status();
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

    @Override
    public ResultData fetchOrderStatus(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderDao.queryOrderStatus(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK){
			return response;
		} else {
			List<OrderStatusVo> list = (List<OrderStatusVo>) response.getData();
			result.setData(list);
			return result;
		}
    }

    @Override
    public ResultData create(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderChannelDao.insert(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	result.setData(response.getData());
		} else {
        	result.setDescription(response.getDescription());
		}
		return result;
    }

	@Override
	public ResultData fetchOrderChannel(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderChannelDao.query(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			return response;
		} else {
			List<OrderChannelVo> list = (List<OrderChannelVo>) response.getData();
			result.setData(list);
			return result;
		}
	}

	@Override
	public ResultData uploadBatch(List<OrderBatch> order) {
		ResultData result = new ResultData();
		ResultData response = orderBatchDao.insert(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
