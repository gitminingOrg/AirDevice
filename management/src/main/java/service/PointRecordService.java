package service;

import model.pointrecord.PointRecord;
import utils.ResultData;

import java.util.Map;

public interface PointRecordService {

    ResultData add(PointRecord pointRecord);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(PointRecord pointRecord);
}
