package device.service;

import model.ReturnCode;

import org.springframework.stereotype.Service;

@Service
public class DeviceInitControlService {
	public ReturnCode enrichHistory(String deviceID){
		//calculate start time & end time
		//select city aqi
		// aggregate data & generate result
		//insert result to table
		//return
		return ReturnCode.FAILURE;
	}
}
