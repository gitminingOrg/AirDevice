package dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.QRCodePreBindDao;
import model.qrcode.PreBindCodeUID;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;

@Repository
public class QRCodePreBindDaoImpl extends BaseDao implements QRCodePreBindDao{
	private Logger logger = LoggerFactory.getLogger(QRCodeDaoImpl.class);
	
	private Object lock = new Object();
	
	@Override
	public ResultData insert(PreBindCodeUID pb) {
		ResultData result = new ResultData();
		pb.setBindId(IDGenerator.generate("PBC"));
		synchronized (lock) {
			try {
				sqlSession.insert("management.qrcode.prebind.insert", pb);
				result.setData(pb);
			}catch(Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			}
		}
		return result;
	}

}
