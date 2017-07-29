package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.ConsumerShareCodeDao;
import model.consumer.ConsumerShareCode;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;

@Repository
public class ConsumerShareCodeDaoImpl extends BaseDaoImpl implements ConsumerShareCodeDao{
	private Logger logger = LoggerFactory.getLogger(ConsumerShareCodeDaoImpl.class);
	
	private Object lock = new Object();
	
	@Override
	public ResultData insert(ConsumerShareCode code) {
		ResultData result = new ResultData();
		code.setCodeId(IDGenerator.generate("CSC"));
		try {
			sqlSession.insert("management.consumer.sharecode.insert", code);
			Map<String, Object> condition = new HashMap<>();
			condition.put("codeId", code.getCodeId());
			ConsumerShareCodeVo vo = sqlSession.selectOne("management.consumer.sharecode.query", condition);
			result.setData(vo);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<ConsumerShareCodeVo> list = sqlSession.selectList("management.consumer.sharecode.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	} 
	

}
