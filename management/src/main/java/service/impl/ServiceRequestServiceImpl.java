package service.impl;

import dao.ServiceRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ServiceRequestService;
import utils.ResultData;
import vo.servicerequest.ServiceRequestVo;

import java.util.List;


@Service
public class ServiceRequestServiceImpl implements ServiceRequestService{

    @Autowired
    private ServiceRequestDao serviceRequestDao;

    @Override
    public ResultData getFeedback() {
        return serviceRequestDao.getFeedBack();
    }

    @Override
    public ResultData getFeedBack(int status) {
        return serviceRequestDao.getFeedBack(status);
    }

    @Override
    public ResultData updateFeedbackStatus(int id, int status) {
        return serviceRequestDao.updateFeedbackStatus(id, status);
    }
}
