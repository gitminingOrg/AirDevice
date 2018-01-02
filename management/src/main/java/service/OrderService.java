package service;

import java.util.List;
import java.util.Map;

import model.order.*;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderService {
	ResultData upload(List<GuoMaiOrder> order);

	ResultData uploadCommodity(List<OrderCommodity> commodityList);

	ResultData fetch(Map<String, Object> condition, DataTableParam param);
	
	ResultData fetch(Map<String, Object> condition);
	
	ResultData assign(GuoMaiOrder order);

	ResultData assignBatchCommodity(List<OrderCommodity> commodityList);

	ResultData blockOrder(Map<String, Object> condition);
	
	ResultData create(GuoMaiOrder order);
	
	ResultData create(OrderMission mission);

	ResultData create(OrderCommodity commodity);
	
	ResultData fetchMission4Order(Map<String, Object> condition);
	
	ResultData fetchChannel();
	
	ResultData fetchStatus();

	ResultData fetchOrderStatus(Map<String, Object> condition);

	ResultData create(OrderChannel orderChannel);

	ResultData fetchOrderChannel(Map<String, Object> condition);

	ResultData uploadBatch(List<GuoMaiOrder> order);

	ResultData fetchMissionChannel(Map<String, Object> condition);

	ResultData create(MissionChannel missionChannel);

	ResultData removeCommodity(Map<String, Object> condition);
}
