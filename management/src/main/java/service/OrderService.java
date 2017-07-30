package service;

import java.util.List;
import java.util.Map;

import model.order.TaobaoOrder;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderService {
	ResultData upload(List<TaobaoOrder> order);

	ResultData fetch(Map<String, Object> condition, DataTableParam param);
	
	ResultData fetch(Map<String, Object> condition);
	
	ResultData assignSerial(TaobaoOrder order);
}
