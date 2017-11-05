package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.BindDao;
import model.qrcode.PreBindCodeUID;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.qrcode.PreBindVO;

@Repository
public class BindDaoImpl extends BaseDao implements BindDao {
	private Logger logger = LoggerFactory.getLogger(BindDaoImpl.class);

	private Object lock = new Object();

	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<PreBindVO> preBindCodeUID = sqlSession.selectList("measurement.bind.query", condition);
			if (preBindCodeUID.size() == 0)
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setData(preBindCodeUID);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData insert(PreBindCodeUID pb) {
		ResultData result = new ResultData();
		pb.setBindId(IDGenerator.generate("PBC"));
		synchronized (lock) {
			try {
				sqlSession.insert("measurement.bind.insert", pb);
				result.setData(pb);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}

}
