package service;

import java.util.List;

import model.order.TaobaoOrder;
import utils.ResultData;

public interface OrderService {
	ResultData upload(List<TaobaoOrder> order);
}
