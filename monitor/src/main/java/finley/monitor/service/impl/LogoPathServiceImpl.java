package finley.monitor.service.impl;

import finley.monitor.dao.LogoPathDao;
import finley.monitor.service.LogoPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ResultData;

import java.util.Map;

@Service
public class LogoPathServiceImpl implements LogoPathService{

    @Autowired
    private LogoPathDao logoPathDao;


    @Override
    public ResultData fetchLogoPath(Map<String, Object> condition) {
        return logoPathDao.queryLogoPath(condition);
    }
}
