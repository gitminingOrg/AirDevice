package service;


import model.userlog.UserLog;
import org.omg.CORBA.Object;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/10/29.
 */
public interface UserLogService {
    public ResultData fetch(Map<String, Object> condition);
}
