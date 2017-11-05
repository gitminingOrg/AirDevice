package service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.QRCodeDao;
import model.qrcode.QRCode;
import service.QRCodeService;
import utils.ResponseCode;
import utils.ResultData;

@Service
public class QRCodeServiceImpl implements QRCodeService {
	private Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);

	@Autowired
	private QRCodeDao qRCodeDao;

	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = qRCodeDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	public ResultData create(QRCode code) {
		ResultData result = new ResultData();
		ResultData response = qRCodeDao.insert(code);
		result.setDescription(response.getDescription());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	public ResultData create(List<QRCode> list) {
		ResultData result = new ResultData();
		for (QRCode item : list) {
			create(item);
		}
		return result;
	}

}
