package schedule;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import config.MeasurementConfig;
import model.qrcode.QRCode;
import service.QRCodeService;
import utils.HttpDeal;

public class QRSyncSchedule {
	
	@Autowired
	private QRCodeService qRCodeService;

	public void schedule() {
		String requestURL = new StringBuffer("http://").append(MeasurementConfig.getValue("upstream_server_url"))
				.append(":").append(MeasurementConfig.getValue("upstream_server_port"))
				.append("/management/qrcode/free/all").toString();
		String response = HttpDeal.getResponse(requestURL);
		JSONObject json = JSONObject.parseObject(response);
		if ("RESPONSE_OK".equals(json.getString("responseCode"))) {
			JSONArray list = json.getJSONArray("data");
			for (int i = 0; i < list.size(); i++) {
				JSONObject current = list.getJSONObject(i);
				QRCode code = new QRCode();
				code.setCodeId(current.getString("codeId"));
				code.setBatchNo(current.getString("batchNo"));
				code.setValue(current.getString("value"));
				code.setPath(current.getString("path"));
				code.setDelivered(Boolean.valueOf(current.getString("codeDelivered")));
				qRCodeService.create(code);
			}
		}
	}
}
