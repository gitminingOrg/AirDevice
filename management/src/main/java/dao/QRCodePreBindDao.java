package dao;

import model.qrcode.PreBindCodeUID;
import utils.ResultData;

public interface QRCodePreBindDao {
	ResultData insert(PreBindCodeUID pb);
	ResultData selectPreBindByQrcode(String qrcode);
	ResultData selectPreBindByUid(String uid);
	ResultData selectChipDeviceByUid(String uid);
}
