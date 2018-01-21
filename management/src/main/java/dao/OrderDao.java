package dao;

import java.util.List;
import java.util.Map;

import model.order.*;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderDao {
	ResultData insert(List<GuoMaiOrder> order);

	ResultData insertOrderItem(List<OrderItem> commodityList);

	ResultData query(Map<String, Object> condition);

	ResultData query(Map<String, Object> condition, DataTableParam param);
	
	ResultData update(GuoMaiOrder order);

	ResultData blockOrder(Map<String, Object> condition);
	
	ResultData create(GuoMaiOrder order);

	ResultData create(OrderItem commodity);

	ResultData channel();
	
	ResultData status();

	ResultData queryOrderStatus(Map<String, Object> condition);

	ResultData updateBatchCommodity(List<OrderItem> commodity);

	ResultData blockCommodity(Map<String, Object> condition);
}
