package service;

import model.pointrecord.PointRecord;
import utils.ResultData;

import java.util.Map;

public interface PointRecordService {

    ResultData create(PointRecord pointRecord);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(PointRecord pointRecord);

    ResultData fetchPointValue(Map<String, Object> condition);
}
