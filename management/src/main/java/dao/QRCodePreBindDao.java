package dao;

import model.qrcode.PreBindCodeUID;
import pagination.DataTableParam;
import pagination.MobilePageParam;
import utils.ResultData;

import java.util.Map;

public interface QRCodePreBindDao {
	ResultData insert(PreBindCodeUID pb);

	ResultData selectPreBindByQrcode(String qrcode);

	ResultData selectPreBindByUid(String uid);

	ResultData selectChipDeviceByUid(String uid);

	ResultData query(Map<String, Object> condition);

	ResultData query(Map<String, Object> condition, DataTableParam param);

	ResultData query(Map<String, Object> condition, MobilePageParam param);

	ResultData deletePreBindByQrcode(String codeId);
}
