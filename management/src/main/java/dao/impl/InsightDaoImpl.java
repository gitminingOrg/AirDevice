package dao.impl;

import dao.BaseDao;
import dao.InsightDao;
import model.machine.Insight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.InsightVo;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 10/12/2017.
 */
@Repository
public class InsightDaoImpl extends BaseDao implements InsightDao {
    private Logger logger = LoggerFactory.getLogger(InsightDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insert(Insight insight) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("management.machine.insight.insert", insight);
            } catch (Exception e) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<InsightVo> list = sqlSession.selectList("management.machine.insight.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            logger.error(e.getMessage());
        }
        return result;
    }
}
