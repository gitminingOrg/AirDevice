package service;

import java.util.List;
import java.util.Map;

import model.order.*;
import pagination.DataTableParam;
import utils.ResultData;

public interface OrderService {
    ResultData upload(List<GuoMaiOrder> order);

    ResultData uploadCommodity(List<OrderItem> commodityList);

    ResultData fetch(Map<String, Object> condition, DataTableParam param);

    ResultData fetch(Map<String, Object> condition);

    ResultData assign(GuoMaiOrder order);

    ResultData assignBatchCommodity(List<OrderItem> commodityList);

    ResultData blockOrder(Map<String, Object> condition);

    ResultData create(GuoMaiOrder order);

    ResultData create(OrderMission mission);

    ResultData create(OrderItem commodity);

    ResultData fetchMission4Order(Map<String, Object> condition);

    ResultData fetchChannel();

    ResultData fetchStatus();

    ResultData fetchOrderStatus(Map<String, Object> condition);

    ResultData create(OrderChannel orderChannel);

    ResultData fetchOrderChannel(Map<String, Object> condition);

    ResultData modifyOrderChannel(OrderChannel orderChannel);

    ResultData deleteOrderChannel(String channelId);

    ResultData fetchMissionChannel(Map<String, Object> condition);

    ResultData create(SetupProvider missionChannel);

    ResultData modifyMissionChannel(SetupProvider missionChannel);

    ResultData deleteMissionChannel(String channelId);

    ResultData removeCommodity(Map<String, Object> condition);
}
