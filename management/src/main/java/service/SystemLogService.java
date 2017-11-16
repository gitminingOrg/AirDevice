package service;

import model.systemlog.SystemLog;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/11/16.
 */
public interface SystemLogService {
    ResultData fetch(Map<String, Object> condition);
    ResultData create(SystemLog systemlog);
}
