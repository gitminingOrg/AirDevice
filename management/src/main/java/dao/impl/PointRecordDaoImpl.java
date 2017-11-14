package dao.impl;

import dao.BaseDao;
import dao.PointRecordDao;
import model.pointrecord.PointRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;

@Repository
public class PointRecordDaoImpl extends BaseDao implements PointRecordDao{

    private Logger logger = LoggerFactory.getLogger(PointRecordDaoImpl.class);
    private final static Object lock = new Object();


    @Override
    public ResultData insert(PointRecord pointRecord) {
        ResultData resultData = new ResultData();
        pointRecord.setRecordId(IDGenerator.generate("PRD"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.pointrecord.insert", pointRecord);
                resultData.setData(pointRecord);
            } catch (Exception e) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                logger.error(e.getMessage());
                resultData.setDescription(e.getMessage());
                return resultData;
            }

        }
        return resultData;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<PointRecord> list = sqlSession.selectList("management.pointrecord.query", condition);
            if (list.size() == 0) {
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
                return resultData;
            }
            resultData.setData(list);
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            logger.error(e.getMessage());
            resultData.setDescription(e.getMessage());
            return resultData;
        }
        return resultData;
    }

    @Override
    public ResultData update(PointRecord pointRecord) {
        return null;
    }
}
