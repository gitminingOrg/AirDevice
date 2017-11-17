package service.impl;

import dao.PointRecordDao;
import model.pointrecord.PointRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.PointRecordService;
import utils.ResponseCode;
import utils.ResultData;
import vo.pointrecord.PointValueVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PointRecordServiceImpl implements PointRecordService {

    @Autowired
    private PointRecordDao pointRecordDao;

    @Override
    public ResultData create(PointRecord pointRecord) {
        return pointRecordDao.insert(pointRecord);
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return pointRecordDao.query(condition);
    }

    @Override
    public ResultData modify(PointRecord pointRecord) {
        return pointRecordDao.update(pointRecord);
    }

    @Override
    public ResultData fetchPointValue(Map<String, Object> condition) {
        ResultData response = pointRecordDao.queryPoint(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ResultData result = new ResultData();
            List<PointValueVO> list = (List) response.getData();

            Map<String, Integer> pointValue = list.stream().collect(
                    Collectors.groupingBy(PointValueVO::getConsumerId,
                            Collectors.summingInt(PointValueVO::getModelBonus))
            );

            result.setData(pointValue);
            return result;
        } else {
            return response;
        }
    }
}
