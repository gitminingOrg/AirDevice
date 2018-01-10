package dao;

import model.order.MissionChannel;
import model.order.OrderChannel;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/12/16.
 */
public interface OrderChannelDao {
    ResultData insert(OrderChannel orderChannel);

    ResultData queryOrderChannel(Map<String, Object> condition);

    ResultData updateOrderChannel(OrderChannel orderChannel);

    ResultData deleteOrderChannel(String channelId);

    ResultData insert(MissionChannel missionChannel);

    ResultData queryMissionChannel(Map<String, Object> condition);

    ResultData updateMissionChannel(MissionChannel missionChannel);

    ResultData deleteMissionChannel(String channelId);
}
