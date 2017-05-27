package device.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import util.ReceptionConstant;
import utils.QRCodeGenerator;
import auth.UserComponent;
import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import device.service.DeviceVipService;

@RequestMapping("/own")
@RestController
public class DeviceVipController {
	private static Logger LOG = LoggerFactory.getLogger(DeviceVipController.class);
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
	public ResultMap shareDevice(@PathVariable("deviceID") String deviceID, @PathVariable("role") int role, HttpServletResponse response){
		ResultMap resultMap = new ResultMap();
		String userID = UserComponent.getUserID();
		DeviceShareCode deviceShareCode = deviceVipService.generateShareCode(userID, deviceID, role, ReceptionConstant.DEFAULT_EXPIRE_DAYS);
		try {
			QRCodeGenerator.createQRCode(deviceShareCode.getToken(), ReceptionConstant.DEFAULT_QR_LENGTH, ReceptionConstant.DEFAULT_QR_LENGTH, response.getOutputStream());
		} catch (IOException e) {
			LOG.error("write QR code failed", e);
		}
		return resultMap;
	}
	
	@RequestMapping("/authorize/{token}")
	public ResultMap authorizeUser(@PathVariable("token") String token){
		ResultMap resultMap = new ResultMap();
		String userID = UserComponent.getUserID();
		ReturnCode returnCode = deviceVipService.authorizeDevice(token, userID);
		if(returnCode.equals(ReturnCode.SUCCESS)){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else if (returnCode.equals(ReturnCode.FORBIDDEN)) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("无权限");
		}else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("授权失败");
		}
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
	
	@RequestMapping("/info/{deviceID}")
	public ResultMap getDeviceInfo(@PathVariable("deviceID") String deviceID){
		ResultMap resultMap = new ResultMap();
		String userID = UserComponent.getUserID();
		DeviceInfo deviceInfo = deviceVipService.getDeviceInfo(userID, deviceID);
		if (deviceInfo == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("无法查询到设备");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE, deviceInfo);
		}
		return resultMap;
	}
	
	@RequestMapping("/all/cities")
	public ResultMap getAllCities(){
		ResultMap resultMap = new ResultMap();
		List<CityList> cityList = deviceVipService.getAllCities();
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		resultMap.addContent("cityList", cityList);
		return resultMap;
	}
}
