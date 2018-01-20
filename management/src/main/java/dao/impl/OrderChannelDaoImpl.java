package dao.impl;

import dao.BaseDao;
import dao.SalesChannelDao;
import model.order.SetupProvider;
import model.order.OrderChannel;
import mybatis.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.SetupProviderVo;
import vo.order.OrderChannelVo;

import java.util.List;
import java.util.Map;


@Repository
public class OrderChannelDaoImpl extends BaseDao implements SalesChannelDao {
    private Logger logger = LoggerFactory.getLogger(OrderChannelDaoImpl.class);

    private final Object lock = new Object();

    @Override
    public ResultData insert(OrderChannel channel) {
        ResultData result = new ResultData();
        channel.setChannelId(IDGenerator.generate("OCL"));
        synchronized (lock) {
            try {
                DataSourceContextHolder.setDbType("event");
                sqlSession.insert("management.order.channel.insert", channel);
                result.setData(new OrderChannelVo(channel));
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
            DataSourceContextHolder.setDbType("event");
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

    @Override
    public ResultData update(OrderChannel channel) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                DataSourceContextHolder.setDbType("event");
                sqlSession.update("management.order.channel.update", channel);
                result.setData(channel);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData delete(String channelId) {
        ResultData result = new ResultData();
        try {
            DataSourceContextHolder.setDbType("event");
            sqlSession.delete("management.order.channel.delete", channelId);
            //result.setDescription("已删除相关信息");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
