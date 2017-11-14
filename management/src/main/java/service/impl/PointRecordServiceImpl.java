package service.impl;

import dao.PointRecordDao;
import model.pointrecord.PointRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.PointRecordService;
import utils.ResultData;

import java.util.Map;


@Service
public class PointRecordServiceImpl implements PointRecordService {

    @Autowired
    private PointRecordDao pointRecordDao;

    @Override
    public ResultData add(PointRecord pointRecord) {
        return pointRecordDao.insert(pointRecord);
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return pointRecordDao.query(condition);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return pointRecordDao.modify(condition);
    }

}
