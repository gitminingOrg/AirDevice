package service;

import java.util.Map;

import model.qrcode.PreBindCodeUID;
import model.qrcode.QRCode;
import pagination.DataTableParam;
import utils.ResultData;

public interface QRCodeService {
	ResultData create(String goodsId, String modelId, String batchNo, int num);

	ResultData fetch(Map<String, Object> condition);
	
	ResultData fetchByBatch(Map<String, Object> condition);
	
	ResultData fetch(Map<String, Object> condition, DataTableParam param);
	
	ResultData deliver(QRCode code);
	
	ResultData prebind(PreBindCodeUID pb);

	ResultData fetchPreBind(Map<String, Object> condition);

	ResultData fetchPreBindByQrcode(String qrcode);

	ResultData fetchPreBindByUid(String uid);

	ResultData fetchDeviceChip(String uid);
}
