package dao;

import model.qrcode.PreBindCodeUID;
import utils.ResultData;

public interface QRCodePreBindDao {
	ResultData insert(PreBindCodeUID pb);
}
