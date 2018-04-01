package finley.monitor.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import finley.monitor.dao.BaseDao;
import finley.monitor.dao.CityPM25Dao;
import finley.monitor.vo.CityPM25Vo;
import utils.ResponseCode;
import utils.ResultData;

@Repository
public class CityPM25DaoImpl extends BaseDao implements CityPM25Dao {
	private Logger logger = LoggerFactory.getLogger(CityPM25DaoImpl.class);

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<CityPM25Vo> list = sqlSession.selectList("monitor.devicecity.queryCityPM25", condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}


	@Override
	public ResultData queryDeviceCity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CityPM25Vo> list = sqlSession.selectList("monitor.devicecity.queryDeviceCity", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
	}
}
