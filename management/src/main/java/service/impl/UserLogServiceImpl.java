package service.impl;

import dao.BaseDao;
import dao.UserLogDao;

import model.userlog.UserLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserLogDao userLogDao;
    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return userLogDao.query(condition);
  }

    @Override
    public ResultData create(UserLog userLog) {
        return userLogDao.insert(userLog);
    }

    @Override
    public ResultData modify(UserLog userlog) {
       return userLogDao.update(userlog);
    }
}
