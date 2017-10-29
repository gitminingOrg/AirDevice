package dao;

import org.omg.CORBA.Object;
import utils.ResultData;
import model.userlog.UserLog;

import java.util.Map;

/**
 * Created by hushe on 2017/10/29.
 */
public interface UserLogDao {
    ResultData query(Map<String, Object> condition);
    ResultData insert(UserLog userlog);
}
