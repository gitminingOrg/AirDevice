package dao.impl;

import dao.BaseDao;
import dao.GmairDao;
import model.GmairOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;

@Repository
public class GmairDaoImpl extends BaseDao implements GmairDao {
    private Logger logger = LoggerFactory.getLogger(GmairDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<GmairOrder> list = sqlSession.selectList("finley.gmair.order.query");
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }

    @Override
    public ResultData update(GmairOrder order) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("finley.gmair.order.update", order);
            result.setData(order);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }
}
