package dao;

import model.order.OrderChannel;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/12/16.
 */
public interface OrderChannelDao {
    ResultData insert(OrderChannel orderChannel);

    ResultData query(Map<String, Object> condition);
}
