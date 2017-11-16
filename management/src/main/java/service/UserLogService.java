package service;


import model.userlog.UserLog;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/10/29.
 */
public interface UserLogService {
    ResultData fetch(Map<String, Object> condition);
    ResultData create(UserLog userLog);
    ResultData modify(UserLog userlog);
}
