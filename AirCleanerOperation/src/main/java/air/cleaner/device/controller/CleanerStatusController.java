package air.cleaner.device.controller;

import javax.servlet.http.HttpServletRequest;

import model.CleanerStatus;
import model.ResultMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.Constant;
import air.cleaner.device.service.DeviceControlService;
import air.cleaner.device.service.DeviceReceiveService;

@RequestMapping(value="/device/status")
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

	@RequestMapping(value="/device")
	public ResultMap getCleanerStatus(HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		CleanerStatus cleanerStatus = deviceReceiveService.getCleanerStatus(device);
		if (cleanerStatus == null) {
			resultMap.setInfo("没有找到相应的设备");;
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("status", cleanerStatus);
		}
		return resultMap;
	}
	
	@RequestMapping(value="/power/{mode}")
	public ResultMap powerControl(@PathVariable("mode")int mode, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
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
	
	@RequestMapping(value="/heat/{mode}")
	public ResultMap heatControl(@PathVariable("mode")int mode, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
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
	
	@RequestMapping(value="/UV/{mode}")
	public ResultMap uvControl(@PathVariable("mode")int mode, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
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
	
	@RequestMapping(value="/mode/{mode}")
	public ResultMap modeControl(@PathVariable("mode")int mode, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.statusControl(Constant.WORKMODE, mode, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/velocity/{velocity}")
	public ResultMap velocityControl(@PathVariable("velocity")int velocity, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
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
	
	@RequestMapping(value="/cycle/{cycle}")
	public ResultMap cycleControl(@PathVariable("cycle")int cycle, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.statusControl(Constant.CYCLE, cycle, device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新状态失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/command/{CTF}/{command}/{data}")
	public ResultMap generalCommand(@PathVariable("CTF")int CTF, @PathVariable("command")String command, @PathVariable("data") int data, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.commandHandler(CTF, command, data, device, CleanerStatus.class);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("获取成功");
		}else{
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("获取失败");
		}
		return resultMap;
	}
}
