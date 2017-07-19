package air.cleaner.device.controller;

import javax.servlet.http.HttpServletRequest;

import model.DeviceInfo;
import model.ResultMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.Constant;
import air.cleaner.device.service.DeviceControlService;
import air.cleaner.device.service.DeviceReceiveService;
@RequestMapping(value="/device/info")
@RestController
public class DeviceInfoController {
	@Autowired
	private DeviceControlService deviceControlService;
	public void setDeviceControlService(DeviceControlService deviceControlService) {
		this.deviceControlService = deviceControlService;
	}
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}

	@RequestMapping(value="/device")
	public ResultMap getDeviceInfo(HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		DeviceInfo deviceInfo = deviceReceiveService.getDeviceInfo(device);
		if (deviceInfo == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("没有找到相应的设备");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("device", deviceInfo);
		}
		return resultMap;
	}
	
	@RequestMapping(value="/server")
	public ResultMap serverIPControl(HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String server = request.getParameter("server");
		String device = request.getParameter("token");
		boolean result = deviceControlService.infoControl(Constant.SERVER_IP, server, device);
		if (!result) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("更新设备连接服务器失败");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新设备连接服务器成功");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/port/{port}")
	public ResultMap serverPortControl(@PathVariable("port")String port, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.infoControl(Constant.SERVER_PORT, port, device);
		if (!result) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("更新服务器端口失败");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新服务器端口成功");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/heartbeat/{gap}")
	public ResultMap heartbeatControl(@PathVariable("gap") int gap, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.infoControl(Constant.HEARTBEAT_GAP, gap, device);
		if (!result) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("更新服务器端口失败");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.setInfo("更新服务器端口成功");
		}		
		return resultMap;
	}
	
	@RequestMapping(value="/command/{CTF}/{command}/{data}")
	public ResultMap generalCommand(@PathVariable("CTF")int CTF, @PathVariable("command")String command, @PathVariable("data") int data, HttpServletRequest request){
		ResultMap resultMap = new ResultMap();
		String device = request.getParameter("token");
		boolean result = deviceControlService.commandHandler(CTF, command, data, device, DeviceInfo.class);
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