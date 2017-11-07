package dao;

import model.qrcode.PreBindCodeUID;
import utils.ResultData;

import java.util.Map;

public interface QRCodePreBindDao {
	ResultData insert(PreBindCodeUID pb);
	ResultData selectPreBindByQrcode(String qrcode);
	ResultData selectPreBindByUid(String uid);
	ResultData selectChipDeviceByUid(String uid);
	ResultData query(Map<String, Object> condition);
}
