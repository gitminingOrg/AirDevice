package service;

import model.guomai.Commodity;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
public interface CommodityService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(Commodity commodity);

    ResultData modify(Commodity commodity);
}
