package device.controller;

import model.ResultMap;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.Constant;
import auth.UserComponent;
import device.service.DeviceStatusService;

@RequestMapping("/status")
@RestController
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
	
	@RequestMapping("/{deviceID}/velocity/{velocity}")
	public ResultMap velocityControl(@PathVariable("deviceID")String deviceID, @PathVariable("velocity")String velocity){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(velocity);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.VELOCITY);
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
	
	@RequestMapping("/{deviceID}/heat/{heat}")
	public ResultMap heatControl(@PathVariable("deviceID")String deviceID, @PathVariable("heat")String heat){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(heat);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.HEAT);
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
	
	@RequestMapping("/{deviceID}/workmode/{mode}")
	public ResultMap workmodeControl(@PathVariable("deviceID")String deviceID, @PathVariable("mode")String mode){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(mode);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.WORKMODE);
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
	
	@RequestMapping("/{deviceID}/cycle/{cycle}")
	public ResultMap cycleControl(@PathVariable("deviceID")String deviceID, @PathVariable("cycle")String cycle){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(cycle);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.CYCLE);
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
	
	@RequestMapping("/{deviceID}/light/{light}")
	public ResultMap lightControl(@PathVariable("deviceID")String deviceID, @PathVariable("light")String light){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(light);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.LIGHT);
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
	
	@RequestMapping("/{deviceID}/uv/{uv}")
	public ResultMap uvControl(@PathVariable("deviceID")String deviceID, @PathVariable("uv")String uv){
		ResultMap resultMap = new ResultMap();
		int data = Integer.parseInt(uv);
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceStatusService.deviceControl(data, deviceID, userID, Constant.UV);
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
