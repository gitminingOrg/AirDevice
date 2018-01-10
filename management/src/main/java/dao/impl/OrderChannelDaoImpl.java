package dao.impl;

import dao.BaseDao;
import dao.OrderChannelDao;
import model.order.MissionChannel;
import model.order.OrderChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.MissionChannelVo;
import vo.order.OrderChannelVo;

import java.util.List;
import java.util.Map;


@Repository
public class OrderChannelDaoImpl extends BaseDao implements OrderChannelDao {

    private Logger logger = LoggerFactory.getLogger(OrderChannelDaoImpl.class);
    private final Object lock = new Object();

    @Override
    public ResultData insert(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        orderChannel.setChannelId(IDGenerator.generate("OC"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.orderChannel.insert", orderChannel);
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
    public ResultData queryOrderChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List <OrderChannelVo> list = sqlSession.selectList("management.order.orderChannel.query", condition);
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
    public ResultData updateOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.order.orderChannel.update", orderChannel);
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
    public ResultData deleteOrderChannel(String channelId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("management.order.orderChannel.delete", channelId);
            result.setDescription("已删除相关信息");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(MissionChannel missionChannel) {
        ResultData result = new ResultData();
        missionChannel.setChannelId(IDGenerator.generate("MC"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.missionChannel.insert", missionChannel);
                result.setData(missionChannel);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData queryMissionChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List <MissionChannelVo> list = sqlSession.selectList("management.order.missionChannel.query", condition);
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
    public ResultData updateMissionChannel(MissionChannel missionChannel) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.order.missionChannel.update", missionChannel);
                result.setData(missionChannel);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData deleteMissionChannel(String channelId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("management.order.missionChannel.delete", channelId);
            result.setDescription("已删除相关信息");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
