package dao;

import java.util.List;

import model.order.TaobaoOrder;
import utils.ResultData;

public interface OrderDao {
	ResultData insert(List<TaobaoOrder> order);
}
