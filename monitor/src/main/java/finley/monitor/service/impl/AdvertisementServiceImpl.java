package finley.monitor.service.impl;

import finley.monitor.dao.AdvertisementDao;
import finley.monitor.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ResultData;

import java.util.Map;

@Service
public class AdvertisementServiceImpl implements AdvertisementService{

    @Autowired
    private AdvertisementDao advertisementDao;
    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return advertisementDao.query(condition);
    }
}
