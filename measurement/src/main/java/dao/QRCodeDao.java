package dao;

import java.util.Map;

import model.qrcode.QRCode;
import utils.ResultData;

public interface QRCodeDao {
	ResultData query(Map<String, Object> condition);
	
	ResultData insert(QRCode code);
}
