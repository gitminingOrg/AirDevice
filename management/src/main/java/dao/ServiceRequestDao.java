package dao;


import vo.servicerequest.ServiceRequestVo;

import java.util.List;

public interface ServiceRequestDao {

    public List<ServiceRequestVo> getFeedBack();
    public List<ServiceRequestVo> getFeedBack(int status);
    public int updateFeedbackStatus(int id, int status);
}
