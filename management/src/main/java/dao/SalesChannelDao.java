package dao;

import model.order.SetupProvider;
import model.order.OrderChannel;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/12/16.
 */
public interface SalesChannelDao {
    ResultData insert(OrderChannel orderChannel);

    ResultData query(Map<String, Object> condition);

    ResultData update(OrderChannel orderChannel);

    ResultData delete(String channelId);
}
