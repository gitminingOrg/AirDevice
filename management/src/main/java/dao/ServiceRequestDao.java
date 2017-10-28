package dao;


import utils.ResultData;

public interface ServiceRequestDao {

    ResultData getFeedBack();
    ResultData getFeedBack(int status);
    ResultData updateFeedbackStatus(int id, int status);
}
