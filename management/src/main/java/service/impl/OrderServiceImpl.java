package service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dao.*;
import model.order.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pagination.DataTableParam;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;
import vo.guomai.CommodityVo;
import vo.order.OrderChannelVo;
import vo.order.OrderCommodityVo;
import vo.order.OrderStatusVo;

@Service
public class OrderServiceImpl implements OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderMissionDao orderMissionDao;

	@Autowired
	private SalesChannelDao salesChannelDao;

	@Autowired
	private OrderBatchDao orderBatchDao;

	@Autowired
	private SetupProviderDao setupProviderDao;

	@Autowired
    private CommodityDao commodityDao;

	@Autowired
    private MachineItemDao machineItemDao;

	@Override
	public ResultData upload(List<GuoMaiOrder> order) {
		ResultData result = new ResultData();
		ResultData response = orderBatchDao.insert(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}


	@Override
	public ResultData uploadOrderItem(List<OrderItem> commodityList) {
		ResultData result = new ResultData();
		ResultData response = orderDao.insertOrderItem(commodityList);
		//create machine item with the order if order item has machine
        List<OrderItem> orderItems = (List<OrderItem>) response.getData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", 0);
            List<CommodityVo> commodityVoList = (List<CommodityVo>) commodityDao.query(condition).getData();
            Map<String, CommodityType> commodityTypeMap =
                    commodityVoList.stream().collect(Collectors.toMap(e -> e.getCommodityId(), e -> e.getType()));
            for (OrderItem item : orderItems) {
                condition.put("commodityId", item.getCommodityId());
                if (commodityTypeMap.get(item.getCommodityId()) == CommodityType.GUOMAI_XINFENG) {
                    MachineItem machineItem = new MachineItem();
                    machineItem.setOrderItemId(item.getOrderItemId());
                    machineItemDao.insert(machineItem);
                }
            }
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
	public ResultData assign(GuoMaiOrder order) {
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
	public ResultData create(GuoMaiOrder order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.create(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
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
	public ResultData create(OrderItem commodity) {
		return orderDao.create(commodity);
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
        ResultData response = salesChannelDao.insert(orderChannel);
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
		ResultData response = salesChannelDao.query(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			return response;
		} else {
			List<OrderChannelVo> list = (List<OrderChannelVo>) response.getData();
			result.setData(list);
			return result;
		}
	}

    @Override
    public ResultData modifyOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = salesChannelDao.update(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	result.setData(response.getData());
		} else {
        	result.setDescription(response.getDescription());
		}
		return result;
    }

	@Override
	public ResultData deleteOrderChannel(String channelId) {
		ResultData result = salesChannelDao.delete(channelId);
		return result;
	}

	@Override
	public ResultData fetchMissionChannel(Map<String, Object> condition) {
		return salesChannelDao.query(condition);
	}

	@Override
	public ResultData create(SetupProvider provider) {
		return setupProviderDao.insert(provider);
	}

    @Override
    public ResultData modifyMissionChannel(SetupProvider provider) {
        ResultData result = new ResultData();
        ResultData response = setupProviderDao.update(provider);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	result.setData(response.getData());
		} else {
        	result.setDescription(response.getDescription());
		}
		return result;
    }

	@Override
	public ResultData deleteMissionChannel(String channelId) {
		ResultData result = salesChannelDao.delete(channelId);
		return result;
	}

	@Override
	public ResultData assignBatchCommodity(List<OrderItem> commodityList) {
		return orderDao.updateBatchCommodity(commodityList);
	}

	@Override
	public ResultData removeCommodity(Map<String, Object> condition) {
		return orderDao.blockCommodity(condition);
	}
}
