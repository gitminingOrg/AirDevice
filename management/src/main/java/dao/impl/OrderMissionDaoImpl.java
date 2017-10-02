package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.OrderMissionDao;
import model.order.OrderMission;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

@Repository
public class OrderMissionDaoImpl extends BaseDao implements OrderMissionDao {
	private Logger logger = LoggerFactory.getLogger(OrderMissionDaoImpl.class);

	@Override
	public ResultData insert(OrderMission mission) {
		ResultData result = new ResultData();
		mission.setMissionId(IDGenerator.generate("MIS"));
		try {
			sqlSession.insert("management.ordermission.insert", mission);
			result.setData(mission);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<OrderMission> list = sqlSession.selectList("management.ordermission.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setDescription(e.getMessage());
		}
		return result;
	}

}
