package finley.monitor.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import finley.monitor.dao.BaseDao;
import finley.monitor.dao.MachineDao;
import finley.monitor.vo.MachineVo;
import utils.ResponseCode;
import utils.ResultData;

@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao {
	private Logger logger = LoggerFactory.getLogger(MachineDaoImpl.class);
	
	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<MachineVo> list = sqlSession.selectList("monitor.machine.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
}
