package dao.impl;

import dao.BaseDao;
import dao.UserLogDao;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;
import model.userlog.UserLog;
/**
 * Created by hushe on 2017/10/29.
 */
@Repository
public class UserLogDaoImpl extends BaseDao implements UserLogDao {
    private Logger logger = LoggerFactory.getLogger(UserLogDaoImpl.class);


    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<UserLogDao> list =
                    sqlSession.selectList("management.userlog.query", condition);
            if(list.isEmpty()){
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            resultData.setData(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }

    @Override
    public ResultData insert(UserLog userlog) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("management.userlog.insert", userlog);
            result.setData(userlog);
        }catch (Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
