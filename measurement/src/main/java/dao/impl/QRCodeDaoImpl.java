package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.QRCodeDao;
import model.qrcode.QRCode;
import utils.ResponseCode;
import utils.ResultData;
import vo.qrcode.QRCodeVo;

@Repository
public class QRCodeDaoImpl extends BaseDao implements QRCodeDao {
	private Logger logger = LoggerFactory.getLogger(QRCodeDaoImpl.class);

	private Object lock = new Object();
	
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<QRCodeVo> list = sqlSession.selectList("measurement.qrcode.query", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData insert(QRCode code) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.insert("measurement.qrcode.insert", code);
				result.setData(code);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}
}
