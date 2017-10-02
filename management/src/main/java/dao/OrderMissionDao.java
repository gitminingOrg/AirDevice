package dao;

import java.util.Map;

import model.order.OrderMission;
import utils.ResultData;

public interface OrderMissionDao {
	ResultData insert(OrderMission mission);

	ResultData query(Map<String, Object> condition);
}
