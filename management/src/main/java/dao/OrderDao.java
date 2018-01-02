package dao;

import java.util.List;
import java.util.Map;

import model.order.*;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderDao {
	ResultData insert(List<GuoMaiOrder> order);

	ResultData insertCommodity(List<OrderCommodity> commodityList);

	ResultData query(Map<String, Object> condition);

	ResultData query(Map<String, Object> condition, DataTableParam param);
	
	ResultData update(GuoMaiOrder order);

	ResultData blockOrder(Map<String, Object> condition);
	
	ResultData create(GuoMaiOrder order);

	ResultData create(OrderCommodity commodity);

	ResultData channel();
	
	ResultData status();

	ResultData queryOrderStatus(Map<String, Object> condition);

	ResultData updateBatchCommodity(List<OrderCommodity> commodity);

	ResultData blockCommodity(Map<String, Object> condition);
}
