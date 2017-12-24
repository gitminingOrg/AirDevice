package dao.impl;

import dao.BaseDao;
import dao.OrderBatchDao;
import model.order.OrderBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;

/**
 * Created by hushe on 2017/12/23.
 */
@Repository
public class OrderBatchDaoImpl extends BaseDao implements OrderBatchDao {
    private Logger logger = LoggerFactory.getLogger(OrderBatchDaoImpl.class);

    @Override
    public ResultData insert(List<OrderBatch> order) {
        ResultData result = new ResultData();
        for (OrderBatch item: order){
            item.setOrderId(IDGenerator.generate("TBO"));
        }
        try {
            sqlSession.insert("management.Batch.OrderBatch", order);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
