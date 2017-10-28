package dao;


import utils.ResultData;
import vo.servicerequest.ServiceRequestVo;

import java.util.List;

public interface ServiceRequestDao {

    ResultData getFeedBack();
    ResultData getFeedBack(int status);
    ResultData updateFeedbackStatus(int id, int status);
}
