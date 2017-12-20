package dao.impl;

import dao.BaseDao;
import dao.SystemLogDao;
import model.systemlog.SystemLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.systemlog.SystemLogVO;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2017/11/16.
 */
public class SystemLogDaoImpl extends BaseDao implements SystemLogDao {
    private Logger logger = LoggerFactory.getLogger(SystemLogDaoImpl.class);
    private Object lock = new Object();

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<SystemLogVO> list =
                    sqlSession.selectList("management.systemlog.query", condition);
            if (list.isEmpty()){
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(SystemLog systemlog) {
        ResultData result = new ResultData();
        systemlog.setLogId(IDGenerator.generate("LOG"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.systemlog.insert", systemlog);
                result.setData(systemlog);
            } catch (Exception e){
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(SystemLog systemlog) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.systemlog.update", systemlog);
                result.setData(systemlog);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
