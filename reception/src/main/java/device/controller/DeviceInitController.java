package device.controller;

import model.ResultMap;
import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import device.service.DeviceInitService;

@RestController
@RequestMapping("/init")
public class DeviceInitController {
	@Autowired
	private DeviceInitService deviceInitService;
	
	@RequestMapping("/enrich/{deviceID}")
	public ResultMap enrichHistory(@PathVariable("deviceID") String deviceID){
		ResultMap resultMap = new ResultMap();
		ReturnCode returnCode = deviceInitService.enrichHistory(deviceID);
		if(returnCode.equals(ReturnCode.SUCCESS)){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}
}
