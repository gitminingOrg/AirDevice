package device.controller;

import model.ResultMap;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import auth.UserComponent;
import utils.Constant;
import device.service.DeviceStatusService;

@RequestMapping("/status")
@Controller
public class DeviceStatusController {
	@Autowired
	private DeviceStatusService deviceStatusService;
	public void setDeviceStatusService(DeviceStatusService deviceStatusService) {
		this.deviceStatusService = deviceStatusService;
	}

	@RequestMapping("/device/{deviceID}")
	public ResultMap getDeviceStatus(@PathVariable("deviceID")String device){
		ResultMap resultMap = new ResultMap();
		
		return resultMap;
	}
	
	@RequestMapping("/{deviceID}/power/{power}")
	public ResultMap powerControl(@PathVariable("deviceID")String deviceID, @PathVariable("power")String power){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(power);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.POWER);
		if (returnCode.equals(ReturnCode.FAILURE)) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("无法远程控制设备");
		}else if (returnCode.equals(ReturnCode.FORBIDDEN)) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("没有权限");
		}else if (returnCode.equals(ReturnCode.SUCCESS)) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}
		return resultMap;
	}
}
