package controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MachineService;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/machine")
public class MachineController {

	private Logger logger = LoggerFactory.getLogger(MachineController.class);

	@Autowired
	private MachineService machineService;

	@RequestMapping(method = RequestMethod.POST, value = "/device/delete/{deviceId}")
	public ResultData delete(@PathVariable String deviceId) {

		ResultData resultData = machineService.deleteDevice(deviceId);
		resultData.setDescription("删除设备：" + deviceId);
		logger.info("delete device using deviceId: " + deviceId);
		return resultData;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idle")
	public ResultData idle() {
		ResultData result = new ResultData();
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
}
