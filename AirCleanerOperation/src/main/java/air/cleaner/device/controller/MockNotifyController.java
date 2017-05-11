package air.cleaner.device.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import air.cleaner.cache.SessionCacheManager;
import air.cleaner.device.service.DeviceControlService;
import air.cleaner.model.ResultMap;

@RestController
public class MockNotifyController {
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	
	@Autowired
	private DeviceControlService deviceControlService;
	public void setDeviceControlService(DeviceControlService deviceControlService) {
		this.deviceControlService = deviceControlService;
	}

	@RequestMapping(value="/assign/locate/{device}")
	public ResultMap getDeviceInfo(@PathVariable("device")String device){
		ResultMap resultMap = new ResultMap();
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		resultMap.addContent("uid", device);
		resultMap.addContent("ip", "localhost");
		return resultMap;
	}
	
	@RequestMapping(value="/device/all")
	public ResultMap getAllCleanerStatus(){
		ResultMap resultMap = new ResultMap();
		Set<String> allDevice = sessionCacheManager.getAllDeviceID();
		resultMap.addContent("devices", allDevice);
		return resultMap;
	}
	
	@RequestMapping(value="/device/restart/{device}")
	public ResultMap restart(@PathVariable("device")String device){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceControlService.restart(device);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}
		return resultMap;
	}
}
