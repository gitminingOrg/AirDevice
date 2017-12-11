package service.impl;

import dao.SystemLogDao;
import model.systemlog.SystemLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SystemLogService;
import utils.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/11/16.
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    private Logger logger = LoggerFactory.getLogger(SystemLogServiceImpl.class);
    @Autowired
    private SystemLogDao systemlogDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return systemlogDao.query(condition);
    }

    @Override
    public ResultData create(SystemLog systemlog) {
        return systemlogDao.insert(systemlog);
    }

    @Override
    public ResultData modify(SystemLog systemlog) {
        return systemlogDao.update(systemlog);
    }
}
