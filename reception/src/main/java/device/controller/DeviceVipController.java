package device.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import location.service.LocationService;
import model.DeviceInfo;
import model.ResultMap;
import model.ReturnCode;
import model.SupportForm;
import model.device.DeviceChip;
import model.location.City;
import model.location.Province;
import model.vip.Consumer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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
import utils.IPUtil;
import utils.QRCodeGenerator;
import vip.service.ConsumerSerivce;
import vo.location.DeviceCityVo;
import vo.vip.ConsumerVo;

import bean.CityList;
import bean.DeviceName;
import bean.DeviceShareCode;
import bean.DeviceStatus;
import bean.UserDevice;
import bean.WechatUser;
import config.ReceptionConfig;
import device.service.DeviceAttributeService;
import device.service.DeviceStatusService;
import device.service.DeviceVipService;
import form.BindDeviceForm;

@RequestMapping("/own")
@RestController
public class DeviceVipController {
	private static Logger LOG = LoggerFactory
			.getLogger(DeviceVipController.class);
	@Autowired
	private DeviceVipService deviceVipService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private ConsumerSerivce consumerSerivce;

	@Autowired
	private DeviceStatusService deviceStatusService;
	
	@Autowired
	private DeviceAttributeService deviceAttributeService;

	// Queue<String> waiting = new LinkedList<>();

	public void setDeviceVipService(DeviceVipService deviceVipService) {
		this.deviceVipService = deviceVipService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public ResultMap register(String openId, BindDeviceForm form) {
		LOG.info("openId: " + openId);
		ResultMap result = new ResultMap();
		if (!StringUtils.isEmpty(openId)) {
			ConsumerVo vo = consumerSerivce.login(openId);
			if (StringUtils.isEmpty(vo)) {
				LOG.info("No user with open_id: " + openId
						+ "found, create a new user...");
				Consumer consumer = new Consumer(openId);
				consumerSerivce.create(consumer);
				try {
					Subject subject = SecurityUtils.getSubject();
					subject.login(new UsernamePasswordToken(openId, ""));
					vo = (ConsumerVo) subject.getPrincipal();
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
			// 绑定用户与设备
			UserDevice ud = new UserDevice(vo.getCustomerId(), form.getSerial());
			ReturnCode returnCode = deviceVipService.bind(ud);
			if (returnCode == ReturnCode.FORBIDDEN) {
				result.setStatus(ResultMap.STATUS_FORBIDDEN);
				result.setInfo("此二维码已被注册");
				return result;
			} else if (returnCode == ReturnCode.FAILURE) {
				result.setStatus(ResultMap.STATUS_FAILURE);
				result.setInfo("绑定失败");
				return result;
			}

			// 设置设备名称
			ReturnCode insert = deviceVipService
					.insertDeviceName(new DeviceName(form.getSerial(), form
							.getAlias(), form.getOwner(), form.getMobile(),
							form.getProvinceID(), form.getCityID(), form
									.getLocation(), 1));
			if (insert == ReturnCode.FORBIDDEN) {
				result.setStatus(ResultMap.STATUS_FORBIDDEN);
				result.setInfo("此二维码已被注册");
				return result;
			} else if (insert == ReturnCode.FAILURE) {
				result.setStatus(ResultMap.STATUS_FAILURE);
				result.setInfo("绑定失败");
				return result;
			}
			
			deviceAttributeService.occupyQRCode(form.getSerial());
			result.setStatus(ResultMap.STATUS_SUCCESS);
		} else {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("当前只支持微信扫码绑定");
			return result;
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/register/available")
	public ResultMap available(String serial) {
		ResultMap result = new ResultMap();
		// if (serial.equals(waiting.peek())) {
		result.setStatus(ResultMap.STATUS_SUCCESS);
		// }
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/register/pop")
	public ResultMap pop() {
		ResultMap result = new ResultMap();
		result.setStatus(ResultMap.STATUS_SUCCESS);
		// result.setInfo("pop top: " + waiting.poll() + ", remaining size: " +
		// waiting.size() + ", current peek: "+ waiting.peek());
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/register/complete")
	public ResultMap complete(String serial, HttpServletRequest request) {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo vo = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(vo)) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("The current user is not authenticated.");
			LOG.error(result.getInfo());
			return result;
		}
		if (StringUtils.isEmpty(serial)) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo(StringUtils.isEmpty(serial) ? "The device serial could not be empty."
					: "The serial code: " + serial + " does not match.");
			LOG.error(result.getInfo());
			return result;
		}

		String ip = IPUtil.tell(request);
		LOG.info("mobile request ip: " + ip);
		String chipId = deviceVipService.getNewChip(ip);
		if (chipId == null) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("当前局域网下无可绑定的设备");
			return result;
		}
		DeviceChip dc = new DeviceChip(serial, chipId);
		deviceStatusService.bindDevice2Chip(dc);
		// waiting.poll();
		result.setStatus(ResultMap.STATUS_SUCCESS);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping("/device")
	public ResultMap getUserDevice(String code, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			if (WechatUtil.isWechat(request) && StringUtils.isEmpty(code)) {
				String targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ ReceptionConfig.getValue("wechat_appid")
						+ "&redirect_uri="
						+ URLEncoder.encode(
								"http://"
										+ ReceptionConfig
												.getValue("domain_url")
										+ ReceptionConfig
												.getValue("mine_device"),
								"utf-8")
						+ "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
				resultMap.setStatus(ResultMap.STATUS_SUCCESS);
				resultMap.addContent("redirect_url", targetUrl);
				return resultMap;
			}
			if (WechatUtil.isWechat(request) && !StringUtils.isEmpty(code)) {
				String openId = WechatUtil.queryOauthOpenId(
						ReceptionConfig.getValue("wechat_appid"),
						ReceptionConfig.getValue("wechat_secret"), code);
				LOG.info("code: " + code + "openID" + openId
						+ "       xxxxxxxxxxxxxxxxxxxxxxx");
				if (!StringUtils.isEmpty(openId)) {
					try {
						subject.login(new UsernamePasswordToken(openId, ""));
						current = (ConsumerVo) subject.getPrincipal();
						String userID = current.getCustomerId();
						List<DeviceStatus> deviceStatus = deviceVipService
								.getUserCleaner(userID);
						if (deviceStatus == null) {
							resultMap.setStatus(ResultMap.STATUS_FAILURE);
							resultMap.setInfo(ResultMap.EMPTY_INFO);
						} else {
							resultMap.setStatus(ResultMap.STATUS_SUCCESS);
							resultMap.addContent(ReceptionConstant.STATUS_LIST,
									deviceStatus);
						}
						return resultMap;
					} catch (Exception e) {
						LOG.error("User " + openId + " login failed.");
					}
				}
			}
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("No user authenticated, please login first.");
			return resultMap;
		}
		String userID = current.getCustomerId();
		List<DeviceStatus> deviceStatus = deviceVipService
				.getUserCleaner(userID);
		if (deviceStatus == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo(ResultMap.EMPTY_INFO);
		} else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.STATUS_LIST, deviceStatus);
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/share/{deviceID}/{role}")
	public ResultMap shareDevice(@PathVariable("deviceID") String deviceID,
			@PathVariable("role") int role, HttpServletResponse response) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		DeviceShareCode deviceShareCode = deviceVipService.generateShareCode(
				userID, deviceID, role, ReceptionConstant.DEFAULT_EXPIRE_DAYS);

		if (deviceShareCode != null) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("token", deviceShareCode.getToken());
			resultMap.addContent("deviceID", deviceShareCode.getDeviceID());
		}
		return resultMap;
	}

	@RequestMapping("/share/token/{token}/{deviceID}/{length}")
	public ResultMap shareQR(@PathVariable("deviceID") String deviceID,
			@PathVariable("token") String token,
			@PathVariable("length") int length, HttpServletResponse response) {
		ResultMap resultMap = new ResultMap();
		// check token
		ReturnCode tokenValid = deviceVipService.checkDeviceShareCode(token,
				deviceID);
		if (!tokenValid.equals(ReturnCode.SUCCESS)) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("二维码无效或已过期");
		}
		// write QR code
		try {
			String url = "http://" + ReceptionConfig.getValue("domain_url")
					+ "/reception/www/index.html#/device/auth/" + token;
			int imgLength = length;
			QRCodeGenerator.createQRCode(url, imgLength, imgLength,
					response.getOutputStream());
		} catch (IOException e) {
			LOG.error("write QR code failed", e);
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/authorize/{token}")
	public ResultMap authorizeUser(@PathVariable("token") String token) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		ReturnCode returnCode = deviceVipService.authorizeDevice(token, userID);
		if (returnCode.equals(ReturnCode.SUCCESS)) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		} else if (returnCode.equals(ReturnCode.FORBIDDEN)) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("无授权权限或您已经拥有此权限");
		} else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("授权失败");
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/device/users/{deviceID}")
	public ResultMap getDeviceUsers(@PathVariable("deviceID") String deviceID) {
		ResultMap resultMap = new ResultMap();
		List<WechatUser> wechatUsers = deviceVipService
				.getDeviceWechatUser(deviceID);
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		resultMap.addContent("wechatUsers", wechatUsers);
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.POST, value = "/device/user/cancel")
	public ResultMap cancelUserPrivilege(String userID, String deviceID) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		String ownerID = current.getCustomerId();
		boolean result = deviceVipService.disableUserDevice(deviceID, userID,
				ownerID);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		} else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}

	@RequestMapping("/wx/authorize")
	public ResultMap wxAuthorizeUser(String openId, String token) {
		ResultMap resultMap = new ResultMap();
		if (!StringUtils.isEmpty(openId)) {
			ConsumerVo vo = consumerSerivce.login(openId);
			if (StringUtils.isEmpty(vo)) {
				LOG.info("No user with open_id: " + openId
						+ "found, create a new user...");
				Consumer consumer = new Consumer(openId);
				consumerSerivce.create(consumer);
				try {
					Subject subject = SecurityUtils.getSubject();
					subject.login(new UsernamePasswordToken(openId, ""));
					vo = (ConsumerVo) subject.getPrincipal();
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}

			ReturnCode returnCode = deviceVipService.authorizeDevice(token,
					vo.getCustomerId());
			if (returnCode.equals(ReturnCode.SUCCESS)) {
				resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			} else if (returnCode.equals(ReturnCode.FORBIDDEN)) {
				resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
				resultMap.setInfo("无授权权限或您已经拥有此权限");
			} else {
				resultMap.setStatus(ResultMap.STATUS_FAILURE);
				resultMap.setInfo("授权失败");
			}
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		} else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("当前只支持微信扫码绑定");
			return resultMap;
		}

		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping(value = "/config/name", method = RequestMethod.POST)
	public ResultMap configName(DeviceName deviceName) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		ReturnCode returnCode = deviceVipService.configDeviceName(userID,
				deviceName);
		if (returnCode.equals(ReturnCode.SUCCESS)) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE_NAME, deviceName);
		} else if (returnCode.equals(ReturnCode.FAILURE)) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("设置设备名称失败");
		} else {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			resultMap.setInfo("无设置权限");
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping(value = "/info/name/{deviceID}")
	public ResultMap getDeviceName(@PathVariable("deviceID") String deviceID) {
		ResultMap resultMap = new ResultMap();
		DeviceName deviceName = deviceVipService.getDeviceName(deviceID);

		if (deviceName == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		} else {
			Province province = locationService.getProvinceByID(deviceName
					.getProvinceID());
			City city = locationService.getCityByID(deviceName.getCityID());
			city.setProvince(province);
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE_NAME, deviceName);
			resultMap.addContent("city", city);
			resultMap.addContent("province", province);
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/info/{deviceID}")
	public ResultMap getDeviceInfo(@PathVariable("deviceID") String deviceID) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		String userID = current.getCustomerId();
		DeviceInfo deviceInfo = deviceVipService
				.getDeviceInfo(userID, deviceID);
		if (deviceInfo == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("无法查询到设备");
		} else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent(ReceptionConstant.DEVICE, deviceInfo);
		}
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/user/role/{deviceID}")
	public ResultMap getUserRole(@PathVariable("deviceID") String deviceID) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		String userID = current.getCustomerId();
		int role = deviceVipService.getUserDeviceRole(userID, deviceID);
		resultMap.addContent("role", role);
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		return resultMap;
	}

	@RequestMapping(value = "/wx/bind/step", method = RequestMethod.GET)
	public ResultMap checkDeviceBindStep(String openId, String serial,
			HttpServletRequest request) {
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo vo = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(vo)) {
			// not login yets
			// check whether a user with the openid already exist in databse
			vo = consumerSerivce.login(openId);
			if (StringUtils.isEmpty(vo)) {
				LOG.info("No user with open_id: " + openId + "found, create a new user...");
				Consumer consumer = new Consumer(openId);
				consumerSerivce.create(consumer);
			}
			try {
				subject.login(new UsernamePasswordToken(openId, ""));
				vo = (ConsumerVo) subject.getPrincipal();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
		//检验二维码是否存在
		boolean qrValid =deviceAttributeService.qrcodeExist(serial);
		if(!qrValid){
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("无效的二维码链接");
			return resultMap;
		}
		String userID = vo.getCustomerId();
		int role = deviceVipService.getUserDeviceRole(userID, serial);
		DeviceName deviceName = deviceVipService.getDeviceName(serial);
		if (deviceName == null) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			// 绑定设备名称步骤
			resultMap.addContent("step", 1);
			return resultMap;
		} else if (role == 0) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			String chipID = deviceStatusService.deviceIDToChipID(serial);
			if (chipID == null) {
				chipID = deviceVipService.getNewChip(IPUtil.tell(request));
				if (chipID != null) {
					boolean result = deviceStatusService
							.bindDevice2Chip(new DeviceChip(serial, chipID));
					if (result) {
						resultMap.addContent("step", 3);
						resultMap.setInfo("初始化设备成功");
						return resultMap;
					} else {
						resultMap.setStatus(ResultMap.STATUS_FAILURE);
						resultMap.setInfo("初始化设备失败");
						return resultMap;
					}
				}
				// wifi初始化步骤
				resultMap.addContent("step", 2);
				return resultMap;
			} else {
				resultMap.setStatus(ResultMap.STATUS_FAILURE);
				resultMap.setInfo("您已经初始化过该设备");
				return resultMap;
			}
		}
		resultMap.setStatus(ResultMap.STATUS_FAILURE);
		resultMap.setInfo("该二维码已被注册");
		return resultMap;
	}

	@RequestMapping("/all/cities")
	public ResultMap getAllCities() {
		ResultMap resultMap = new ResultMap();
		Map<String, Object> condition = new HashMap<>();
		List<DeviceCityVo> list = locationService.fetch(condition);
		if (list.isEmpty()) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("no city record");
			return resultMap;
		}
		for (DeviceCityVo item : list) {
			item.setInitial((item.getCityPinyin().charAt(0) + "").toUpperCase());
		}
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		resultMap.addContent(ReceptionConstant.CITY_LIST, list);
		return resultMap;
	}

	@RequiresAuthentication
	@RequestMapping("/support")
	public ResultMap support(SupportForm supportForm) {
		ResultMap resultMap = new ResultMap();
		boolean result = deviceVipService.submitSupportForm(supportForm);
		if (result) {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		} else {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/user/location/{cityPinyin}")
	public ResultMap userLocation(@PathVariable("cityPinyin")String cityPinyin){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		boolean update = deviceVipService.updateUserLocation(current.getCustomerId(), cityPinyin);
		if(!update){
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/user/city")
	public ResultMap userCity(){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		String city = deviceVipService.getUserCity(current.getCustomerId());
		if(city == null){
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("city", city);
		}
		return resultMap;
	}


	@RequiresAuthentication
	@RequestMapping("/subscribe/{openID}")
	public ResultMap checkSubscribe(@PathVariable("openID")String openID){
		ResultMap resultMap = new ResultMap();
		boolean result = deviceVipService.checkSubscribe(openID);
		if(result){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else{
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/share/data/{deviceID}")
	public ResultMap generateShareDataPic(@PathVariable("deviceID")String deviceID){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		boolean result = deviceVipService.generateShareImage(current.getCustomerId(), deviceID);
		if(result){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else{
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}
	
	@RequiresAuthentication
	@RequestMapping("/coupon/{code}")
	public ResultMap generateCouponPic(@PathVariable("code")String code){
		ResultMap resultMap = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (current == null) {
			resultMap.setStatus(ResultMap.STATUS_FORBIDDEN);
			return resultMap;
		}
		boolean result = deviceVipService.generateCouponImage(current.getCustomerId(), code);
		if(result){
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		}else{
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
		}
		return resultMap;
	}
}
