package dao.impl;

import dao.BaseDao;
import dao.OrderChannelDao;
import model.order.OrderChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.OrderChannelVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2017/12/16.
 */
@Repository
public class OrderChannelDaoImpl extends BaseDao implements OrderChannelDao {

    private Logger logger = LoggerFactory.getLogger(OrderChannelDaoImpl.class);
    private Object lock = new Object();

    @Override
    public ResultData insert(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.channel.insert", orderChannel);
                result.setData(orderChannel);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List <OrderChannelVo> list = sqlSession.selectList("management.order.channel.query", condition);
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
}
