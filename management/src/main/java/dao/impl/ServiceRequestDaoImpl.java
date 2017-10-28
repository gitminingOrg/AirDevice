package dao.impl;

import dao.BaseDao;
import dao.ServiceRequestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;
import vo.servicerequest.ServiceRequestVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ServiceRequestDaoImpl extends BaseDao implements ServiceRequestDao{
    Logger logger = LoggerFactory.getLogger(ServiceRequestDaoImpl.class);
    private final Object lock = new Object();

    // 获取用户的反馈信息
    @Override
    public ResultData getFeedBack() {
        ResultData result = new ResultData();
        try {
            List<ServiceRequestVo> serviceRequestVos =
                    sqlSession.selectList("management.feedback.selectFeedBack");
            result.setData(serviceRequestVos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    // 获取某种状态的用户反馈
    @Override
    public ResultData getFeedBack(int status) {
        ResultData result = new ResultData();
        try {
            List<ServiceRequestVo> serviceRequestVos =
                    sqlSession.selectList("management.feedback.selectFeedBackByStatus", status);
            result.setData(serviceRequestVos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    // 更新反馈的状态
    @Override
    public ResultData updateFeedbackStatus(int id, int status) {
        synchronized (lock) {
            ResultData result = new ResultData();
            try {
                Map<String, Integer> params = new HashMap<>();
                params.put("id", id);
                params.put("status", status);
                sqlSession.update("management.feedback.updateFeedBackStatus", params);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return result;
            }
            return result;
        }
    }

}
