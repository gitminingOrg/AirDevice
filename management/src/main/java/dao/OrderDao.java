package dao;

import java.util.List;
import java.util.Map;

import model.order.CustomizeOrder;
import model.order.TaobaoOrder;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderDao {
	ResultData insert(List<TaobaoOrder> order);

	ResultData query(Map<String, Object> condition);

	ResultData query(Map<String, Object> condition, DataTableParam param);
	
	ResultData update(TaobaoOrder order);

	ResultData blockOrder(Map<String, Object> condition);
	
	ResultData create(CustomizeOrder order);
	
	ResultData channel();
	
	ResultData status();

	ResultData queryOrderStatus(Map<String, Object> condition);
}
