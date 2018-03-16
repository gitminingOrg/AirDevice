package service;

import model.GmairOrder;
import utils.ResultData;

import java.util.Map;

public interface GMorderService {
    ResultData fetchOrder(Map<String, Object> condition);

    ResultData modifyOrder(GmairOrder order);
}
