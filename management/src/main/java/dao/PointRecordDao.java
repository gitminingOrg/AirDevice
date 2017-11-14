package dao;


import model.pointrecord.PointRecord;
import utils.ResultData;

import java.util.Map;

public interface PointRecordDao {

    ResultData insert(PointRecord pointRecord);

    ResultData query(Map<String, Object> condition);

    ResultData update(PointRecord pointRecord);
}
