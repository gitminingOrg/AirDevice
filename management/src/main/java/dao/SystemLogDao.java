package dao;

import model.systemlog.SystemLog;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/11/16.
 */

public interface SystemLogDao {
    ResultData query(Map<String, Object> condition);
    ResultData insert(SystemLog systemlog);
    ResultData update(SystemLog systemlog);
}
