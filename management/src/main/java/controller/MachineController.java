package controller;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.machine.IdleMachine;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.MachineService;
import service.QRCodeService;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.IdleMachineVo;
import vo.machine.MachineStatusVo;

@CrossOrigin
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
		Set<String> onlineMachine = new HashSet<>();
		try {
			String response = HttpDeal.getResponse(listUrl);
			JSONObject json = JSONObject.parseObject(response);
			if (!StringUtils.isEmpty(json.get("contents"))
					&& !StringUtils.isEmpty(json.getJSONObject("contents").get("devices"))) {
				JSONArray uids = json.getJSONObject("contents").getJSONArray("devices");
				for (int i = 0; i < uids.size(); i++) {
					String session = uids.getString(i);
					String uid = session.replaceAll("session.", "");
					onlineMachine.add(uid);
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
			List<IdleMachineVo> list = (List<IdleMachineVo>) response.getData();
			list = list.stream().filter(e -> onlineMachine.contains(e.getUid())).collect(Collectors.toList());
			result.setData(list);
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
				info.put("velocity", json.getJSONObject("contents").getJSONObject("status").get("velocity"));
			}
			result.setData(info);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/device/status")
	public ResultData status(@RequestParam(required = false) String param) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		if (!StringUtils.isEmpty(param)) {
			JSONObject paramJsonObject = JSON.parseObject(param);
			if (!StringUtils.isEmpty(paramJsonObject.get("startDate"))) {
				condition.put("startTime", paramJsonObject.getString("startDate"));
			}
			if (!StringUtils.isEmpty(paramJsonObject.get("endDate"))) {
				condition.put("endTime", paramJsonObject.getString("endDate"));
			}
		}
		ResultData response = machineService.queryMachineStatus(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("服务器忙，请稍后再试！");
		} else {
			result.setData(response.getData());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/device/status/table")
	public DataTablePage<MachineStatusVo> machineStatusByPage(DataTableParam param) {
		DataTablePage<MachineStatusVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData response = machineService.queryMachineStatus(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<MachineStatusVo>) response.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idleupdate")
	public ResultData update4pre() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData response = machineService.mdifyidle(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("服务器忙，请稍后再试");
			return result;
		}
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			result.setData(response.getData());
			return result;
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/record/list")
	public ResultData Record() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData response = machineService.fetchRecord(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("服务器忙，请稍后再试");
		} else {
			result.setData(response.getData());
		}
		return result;
	}
}
