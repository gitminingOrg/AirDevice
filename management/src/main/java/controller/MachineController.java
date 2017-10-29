package controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.machine.IdleMachine;
import service.MachineService;
import service.QRCodeService;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/machine")
public class MachineController {

	private Logger logger = LoggerFactory.getLogger(MachineController.class);

	@Autowired
	private MachineService machineService;

	@Autowired
	private QRCodeService qRCodeService;

	@RequestMapping(method = RequestMethod.POST, value = "/device/delete/{deviceId}")
	public ResultData delete(@PathVariable String deviceId) {

		ResultData resultData = machineService.deleteDevice(deviceId);
		logger.info("delete device using deviceId: " + deviceId);
		return resultData;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idle")
	public ResultData idle() {
		ResultData result = new ResultData();
		String listUrl = "http://commander.gmair.net/AirCleanerOperation/device/all";
		try {
			String response = HttpDeal.getResponse(listUrl);
			JSONObject json = JSONObject.parseObject(response);
			if (!StringUtils.isEmpty(json.get("contents"))
					&& !StringUtils.isEmpty(json.getJSONObject("contents").get("devices"))) {
				JSONArray uids = json.getJSONObject("contents").getJSONArray("devices");
				for (int i = 0; i < uids.size(); i++) {
					String session = uids.getString(i);
					String uid = session.replaceAll("session.", "");
					ResultData rs = qRCodeService.fetchPreBindByUid(uid);
					if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
						continue;
					}
					if (rs.getResponseCode() == ResponseCode.RESPONSE_NULL) {
						IdleMachine im = new IdleMachine(uid);
						rs = machineService.createIdleMachine(im);
						if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
							logger.debug("机器编码:" + uid + "被记录为idle状态的机器");
						} else {
							logger.debug("机器编码:" + uid + "录入失败");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData response = machineService.fetchIdleMachine(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("服务器异常，请稍后尝试");
			return result;
		}
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			result.setData(response.getData());
			return result;
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/idle/update")
	public ResultData idleUpdate(@RequestParam String imId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		condition.put("im_id", imId);
		ResultData response = machineService.updateIdleMachine(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("服务器异常，请稍后尝试");
			return result;
		}
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			result.setData(response.getData());
			return result;
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "{uid}/detail")
	public ResultData detail(@PathVariable("uid") String uid) {
		ResultData result = new ResultData();
		String url = new StringBuffer("http://commander.gmair.net/AirCleanerOperation/device/status/device?token=")
				.append(uid).toString();
		try {
			String response = HttpDeal.getResponse(url);
			JSONObject json = JSONObject.parseObject(response);
			JSONObject info = new JSONObject();
			if (!StringUtils.isEmpty(json.get("contents"))
					&& !StringUtils.isEmpty(json.getJSONObject("contents").get("status"))) {
				info.put("ip", json.getJSONObject("contents").getJSONObject("status").get("ip"));
				info.put("time", json.getJSONObject("contents").getJSONObject("status").get("time"));
			}
			result.setData(info);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
}
