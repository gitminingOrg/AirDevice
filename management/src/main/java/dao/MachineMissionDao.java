package dao;

import model.order.MachineMission;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/17.
 */
public interface MachineMissionDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(MachineMission machineMission);
}
