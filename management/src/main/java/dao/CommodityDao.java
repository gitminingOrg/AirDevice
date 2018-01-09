package dao;

import model.guomai.Commodity;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
public interface CommodityDao {
    ResultData query(Map<String, Object> condition);
    ResultData insert(Commodity commodity);
    ResultData update(Commodity commodity);
}
