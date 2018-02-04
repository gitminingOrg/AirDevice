package dao.impl;

import dao.BaseDao;
import dao.OrderDiversionDao;
import model.order.OrderDiversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.OrderDiversionVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/22.
 */
@Repository
public class OrderDiversionDaoImpl extends BaseDao implements OrderDiversionDao {

    private Logger logger = LoggerFactory.getLogger(OrderDiversionDaoImpl.class);
    private Object lock = new Object();
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderDiversionVo> list = sqlSession.selectList("management.order.diversion.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        synchronized (lock) {
            orderDiversion.setDiversionId(IDGenerator.generate("OD"));
            try {
                sqlSession.insert("management.order.diversion.insert", orderDiversion);
                result.setData(orderDiversion);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.order.diversion.update", orderDiversion);
                result.setData(orderDiversion);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData delete(String diversionId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("management.order.diversion.delete", diversionId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
