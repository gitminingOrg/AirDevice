package device.controller;

import java.util.List;

import model.ResultMap;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import util.ReceptionConstant;
import auth.UserComponent;
import bean.DeviceName;
import bean.DeviceStatus;
import device.service.DeviceVipService;

@RequestMapping("/own")
@RestController
public class DeviceVipController {
	@Autowired
	private DeviceVipService deviceVipService;
	public void setDeviceVipService(DeviceVipService deviceVipService) {
		this.deviceVipService = deviceVipService;
	}

	@RequestMapping("/device")
	public ResultMap getUserDevice(){
		ResultMap resultMap = new ResultMap();
		String userID = UserComponent.getUserID();
		List<DeviceStatus> deviceStatus = deviceVipService.getUserCleaner(userID);
		if (deviceStatus == null || deviceStatus.size() == 0) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo(ResultMap.EMPTY_INFO);
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.STATUS_LIST, deviceStatus);
		}
		return resultMap;
	}
	
	@RequestMapping("/share/{deviceID}/{role}")
	public ResultMap shareDevice(@PathVariable("deviceID") String deviceID, @PathVariable("role") String role){
		ResultMap resultMap = new ResultMap();
		return resultMap;
	}
	
	@RequestMapping("/authorize/{token}")
	public ResultMap authorizeUser(@PathVariable("token") String token){
		ResultMap resultMap = new ResultMap();
		
		return resultMap;
	}
	
	@RequestMapping(value = "/name", method= RequestMethod.POST)
	public ResultMap configName(@RequestBody DeviceName deviceName){
		ResultMap resultMap = new ResultMap();
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceVipService.configDeviceName(userID, deviceName);
		if (returnCode.equals(ReturnCode.SUCCESS)) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE_NAME, deviceName);
		}else if (returnCode.equals(ReturnCode.FAILURE)) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("设置设备名称失败");
		}else {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("无设置权限");
		}
		return resultMap;
	}
	
	@RequestMapping("/{deviceID}/info")
	public ResultMap getDeviceInfo(@PathVariable("deviceID") String deviceID){
		ResultMap resultMap = new ResultMap();
		return resultMap;
	}
}
