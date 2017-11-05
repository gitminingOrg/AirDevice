package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.flow.builder.ReturnBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import config.MeasurementConfig;
import model.machine.IdleMachine;
import model.qrcode.PreBindCodeUID;
import schedule.QRSyncSchedule;
import service.BindService;
import service.MachineService;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;
import vo.machine.IdleMachineVo;

@RestController
@RequestMapping("/machine")
public class MachineController {
	private Logger logger = LoggerFactory.getLogger(MachineController.class);

	@Autowired
	private BindService bindService;

	@Autowired
	private MachineService machineService;

	@Autowired
	private QRSyncSchedule qRSyncSchedule;

	@RequestMapping(method = RequestMethod.GET, value = "/bind")
	public ModelAndView bind() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/qrcode/prebind");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idle")
	public ResultData idle() {
		ResultData result = new ResultData();
		String requestURL = new StringBuffer("http://").append(MeasurementConfig.getValue("server_url")).append(":")
				.append(MeasurementConfig.getValue("server_port")).append("/AirCleanerOperation/device/all").toString();
		try {
			String response = HttpDeal.getResponse(requestURL);
			JSONObject json = JSONObject.parseObject(response);
			if (!StringUtils.isEmpty(json.get("contents"))
					&& !StringUtils.isEmpty(json.getJSONObject("contents").get("devices"))) {
				JSONArray uids = json.getJSONObject("contents").getJSONArray("devices");
				for (int i = 0; i < uids.size(); i++) {
					String session = uids.getString(i);
					String uid = session.replaceAll("session.", "");
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("uid", uid);
					ResultData rs = bindService.fetchPrebind(condition);
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
		Map<String, Object> condition = new HashMap<String, Object>();
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
		result.setResponseCode(ResponseCode.RESPONSE_NULL);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/bind")
	public ResultData bind(String uid, String codeId) {
		ResultData result = new ResultData();
		PreBindCodeUID pb = new PreBindCodeUID(uid, codeId);
		ResultData response = bindService.createPreBind(pb);
		result.setResponseCode(response.getResponseCode());
		if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
			Map<String, Object> condition = new HashMap<>();
			condition.put("uid", uid);
			response = machineService.fetchIdleMachine(condition);
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
				IdleMachineVo vo = ((List<IdleMachineVo>) response.getData()).get(0);
				condition.clear();
				condition.put("im_id", vo.getImId());
				response = machineService.updateIdleMachine(condition);
			}
			result.setData(response.getData());

		} else if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public ResultData list() {
		ResultData result = new ResultData();

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/sync")
	public ResultData qrSync() {
		ResultData result = new ResultData();
		qRSyncSchedule.schedule();
		return result;
	}
}
