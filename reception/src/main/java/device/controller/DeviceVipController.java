package device.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.enterprise.event.Reception;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import util.ReceptionConstant;
import util.WechatUtil;
import utils.QRCodeGenerator;
import vo.vip.ConsumerVo;
import auth.UserComponent;
import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import config.ReceptionConfig;
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

	@RequiresAuthentication
	@RequestMapping("/device")
	public ResultMap getUserDevice(String code, HttpServletRequest request, HttpServletResponse response) throws IOException{
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if(StringUtils.isEmpty(current)) {
			if(WechatUtil.isWechat(request) && StringUtils.isEmpty(code)) {
				String targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + ReceptionConfig.getValue("wechat_appid") + "&redirect_uri=" + URLEncoder.encode("http://" + ReceptionConfig.getValue("domain_url") + ReceptionConfig.getValue("mine_device"), "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
				response.sendRedirect(targetUrl);
			}
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("No user authenticated, please login first.");
			return resultMap;
		}
		String userID = current.getCustomerId();
		List<DeviceStatus> deviceStatus = deviceVipService.getUserCleaner(userID);
		if (deviceStatus == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo(ResultMap.EMPTY_INFO);
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.STATUS_LIST, deviceStatus);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/share/{deviceID}/{role}")
	public ResultMap shareDevice(@PathVariable("deviceID") String deviceID, @PathVariable("role") int role, HttpServletResponse response){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		DeviceShareCode deviceShareCode = deviceVipService.generateShareCode(userID, deviceID, role, ReceptionConstant.DEFAULT_EXPIRE_DAYS);
		try {
			QRCodeGenerator.createQRCode(deviceShareCode.getToken(), ReceptionConstant.DEFAULT_QR_LENGTH, ReceptionConstant.DEFAULT_QR_LENGTH, response.getOutputStream());
		} catch (IOException e) {
			LOG.error("write QR code failed", e);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/authorize/{token}")
	public ResultMap authorizeUser(@PathVariable("token") String token){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		ReturnCode returnCode = deviceVipService.authorizeDevice(token, userID);
		if(returnCode.equals(ReturnCode.SUCCESS)){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else if (returnCode.equals(ReturnCode.FORBIDDEN)) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("无授权权限或您已经拥有此权限");
		}else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("授权失败");
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping(value = "/config/name", method= RequestMethod.POST)
	public ResultMap configName(DeviceName deviceName){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
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
	
	@RequiresAuthentication
	@RequestMapping(value = "/info/name/{deviceID}")
	public ResultMap getDeviceName(@PathVariable("deviceID") String deviceID){
		ResultMap resultMap = new ResultMap();
		DeviceName deviceName = deviceVipService.getDeviceName(deviceID);
		if(deviceName == null){
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE_NAME, deviceName);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/info/{deviceID}")
	public ResultMap getDeviceInfo(@PathVariable("deviceID") String deviceID){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
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
		resultMap.addContent(ReceptionConstant.CITY_LIST, cityList);
		return resultMap;
	}
}
