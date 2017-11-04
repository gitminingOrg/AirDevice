package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
import dao.QRCodePrebindDao;
import utils.ResponseCode;
import utils.ResultData;
import vo.qrcode.PreBindVO;

@Repository
public class QRCodePrebindDaoImpl extends BaseDaoImpl implements QRCodePrebindDao {
	private Logger logger = LoggerFactory.getLogger(QRCodePrebindDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		synchronized (lock) {
			List<PreBindVO> list = sqlSession.selectList("reception.qrcode.prebind.query", condition);
			if (list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}
		return result;
	}

}
