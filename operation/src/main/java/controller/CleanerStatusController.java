package controller;

import model.CleanerStatus;
import model.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.DeviceControlService;
import service.DeviceReceiveService;
import utils.Constant;

@RequestMapping(value="/status")
@RestController
public class CleanerStatusController {
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}
	@Autowired
	private DeviceControlService deviceControlService;
	public void setDeviceControlService(DeviceControlService deviceControlService) {
		this.deviceControlService = deviceControlService;
	}

	@RequestMapping(value="/device/{deviceID}")
	public ResultMap getCleanerStatus(@PathVariable("deviceID")String deviceString){
		ResultMap resultMap = new ResultMap();
		long deviceID = Long.parseLong(deviceString);
		CleanerStatus cleanerStatus = deviceReceiveService.getCleanerStatus(deviceID);
		if (cleanerStatus == null) {
			resultMap.setInfo("没有找到相应的设备");;
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("status", cleanerStatus);
		}
		return resultMap;
	}
	
	@RequestMapping(value="/power/{deviceID}/{mode}")
	public ResultMap powerControl(@PathVariable("deviceID")long device, @PathVariable("mode")int mode){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.statusControl(Constant.POWER, mode, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/heat/{deviceID}/{mode}")
	public ResultMap heatControl(@PathVariable("deviceID")long device, @PathVariable("mode")int mode){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.statusControl(Constant.HEAT, mode, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/UV/{deviceID}/{mode}")
	public ResultMap uvControl(@PathVariable("deviceID")long device, @PathVariable("mode")int mode){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.statusControl(Constant.UV, mode, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/mode/{deviceID}/{mode}")
	public ResultMap modeControl(@PathVariable("deviceID")long device, @PathVariable("mode")int mode){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.statusControl(Constant.UV, mode, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/velocity/{deviceID}/{velocity}")
	public ResultMap velocityControl(@PathVariable("deviceID")long device, @PathVariable("velocity")int velocity){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.statusControl(Constant.VELOCITY, velocity, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
}
