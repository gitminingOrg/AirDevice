package service;


import utils.ResultData;
import vo.servicerequest.ServiceRequestVo;

import java.util.List;

public interface ServiceRequestService {

    ResultData getFeedback();

    ResultData getFeedBack(int status);

    ResultData updateFeedbackStatus(int id, int status);
}
