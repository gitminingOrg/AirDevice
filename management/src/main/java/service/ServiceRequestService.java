package service;


import utils.ResultData;

public interface ServiceRequestService {

    ResultData getFeedback();

    ResultData getFeedBack(int status);

    ResultData updateFeedbackStatus(int id, int status);
}
