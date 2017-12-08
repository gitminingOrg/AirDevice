package dao;


import java.util.Map;

import model.qrcode.QRCode;
import pagination.DataTableParam;
import utils.ResultData;

public interface QRCodeDao {
	ResultData insert(QRCode code);
	
	ResultData query(Map<String, Object> condition);
	
	ResultData queryByBatch(Map<String, Object> condition);
	
	ResultData query(Map<String, Object> condition, DataTableParam param);
	
	ResultData udpate(QRCode code);

	ResultData queryQrcodeStatus(Map<String, Object> condition);
}
