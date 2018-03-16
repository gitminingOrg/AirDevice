package dao;

import model.GmairOrder;
import utils.ResultData;

import java.util.Map;

public interface GmairDao {
    ResultData query(Map<String, Object> condition);

    ResultData update(GmairOrder order);
}
