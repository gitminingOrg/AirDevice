package dao;

import model.order.OrderDiversion;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/22.
 */
public interface OrderDiversionDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(OrderDiversion orderDiversion);

    ResultData update(OrderDiversion orderDiversion);

    ResultData delete(String diversionId);
}
