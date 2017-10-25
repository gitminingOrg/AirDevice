package dao.impl;

import dao.BaseDao;
import dao.ServiceRequestDao;
import org.springframework.stereotype.Repository;
import vo.servicerequest.ServiceRequestVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ServiceRequestDaoImpl extends BaseDao implements ServiceRequestDao{

    // 获取用户的反馈信息
    @Override
    public List<ServiceRequestVo> getFeedBack() {
        return sqlSession.selectList("management.feedback.selectFeedBack");
    }

    // 获取某种状态的用户反馈
    @Override
    public List<ServiceRequestVo> getFeedBack(int status) {
        return sqlSession.selectList("management.feedback.selectFeedBackByStatus", status);
    }

    // 更新反馈的状态
    @Override
    public int updateFeedbackStatus(int id, int status) {
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        return sqlSession.update("management.feedback.updateFeedBackStatus", params);
    }

}
