package dao;

import model.machine.Insight;
import utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 10/12/2017.
 */
public interface InsightDao {
    ResultData insert(Insight insight);

    ResultData query(Map<String, Object> condition);
}
