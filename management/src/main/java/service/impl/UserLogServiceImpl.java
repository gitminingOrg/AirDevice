package service.impl;

import dao.BaseDao;
import dao.UserLogDao;

import model.userlog.UserLog;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import service.UserLogService;
import utils.ResponseCode;
import utils.ResultData;


import java.util.Map;


/**
 * Created by hushe on 2017/10/29.
 */
@Repository
public class UserLogServiceImpl implements UserLogService {
    private Logger logger = LoggerFactory.getLogger(UserLogService.class);

    private UserLogDao userLogDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = userLogDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
