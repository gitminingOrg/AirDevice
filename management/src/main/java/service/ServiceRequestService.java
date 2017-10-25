package service;


import vo.servicerequest.ServiceRequestVo;

import java.util.List;

public interface ServiceRequestService {

    public List<ServiceRequestVo> getFeedback();

    public List<ServiceRequestVo> getFeedBack(int status);

    public int updateFeedbackStatus(int id, int status);
}
