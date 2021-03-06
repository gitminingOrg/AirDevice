package dao.impl;

import dao.BaseDao;
import dao.UserLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;
import model.userlog.UserLog;
import vo.userlog.UserLogVO;

/**
 * Created by hushe on 2017/10/29.
 */
@Repository
public class UserLogDaoImpl extends BaseDao implements UserLogDao {
    private Logger logger = LoggerFactory.getLogger(UserLogDaoImpl.class);
    private Object lock = new Object();

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<UserLogVO> list =
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
        userlog.setLogId(IDGenerator.generate("LOG"));
        ResultData result = new ResultData();
        synchronized (lock){
            try {
                sqlSession.insert("management.userlog.insert", userlog);
                result.setData(userlog);
            }catch (Exception e){
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(UserLog userlog) {
        ResultData result = new ResultData();
        synchronized (lock){
            try {
                sqlSession.update("management.userlog.update", userlog);
                result.setData(userlog);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
