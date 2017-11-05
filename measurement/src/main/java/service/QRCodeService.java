package service;

import java.util.List;
import java.util.Map;

import model.qrcode.QRCode;
import utils.ResultData;

public interface QRCodeService {
	ResultData fetch(Map<String, Object> condition);

	ResultData create(QRCode code);

	ResultData create(List<QRCode> list);
}
