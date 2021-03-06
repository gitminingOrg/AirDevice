package device.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import device.service.DeviceStatusService;
import location.service.impl.LocationServiceImpl;
import model.CleanerStatus;
import model.ResultMap;
import model.location.City;
import model.location.Province;
import utils.IPUtil;
import vo.location.DeviceCityVo;

@RestController
@RequestMapping("/location")
public class LocationController {
	private Logger logger = LoggerFactory.getLogger(LocationController.class);
	
	@Autowired
	private DeviceStatusService deviceStatusService;
	
	@Autowired
	private LocationServiceImpl locationService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{deviceId}")
	public ResultMap location(@PathVariable("deviceId") String deviceId) {
		ResultMap result = new ResultMap();
		if(StringUtils.isEmpty(deviceId)) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("You must have a legal device serial number");
			return result;
		}
		CleanerStatus status = deviceStatusService.getCleanerStatus(deviceId);
		String ip = status.getIp();
		String adcode = locationService.locateByIp(ip);
		Map<String, Object> condition = new HashMap<>();
		condition.put("cityId", adcode);
		List<DeviceCityVo> list = locationService.fetch(condition);
		if(list.isEmpty()) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("未能定位到设备ip: " + ip + "所对应的地区");
			return result;
		}
		DeviceCityVo location = list.get(0);
//		DeviceCityVo location = new DeviceCityVo();
//		location.setCityId("10284");
//		location.setCityName("北京");
//		location.setCityPinyin("beijing");
//		location.setProvinceName("北京");
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("location", location);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/phone")
	public ResultMap locatePhone(HttpServletRequest request) {
		ResultMap result = new ResultMap();
		String ip = IPUtil.tell(request);
		String adcode = locationService.locateByIp(ip);
		Map<String, Object> condition = new HashMap<>();
		condition.put("cityId", adcode);
		List<DeviceCityVo> list = locationService.fetch(condition);
		if(list.isEmpty()) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("未能定位到设备ip: " + ip + "所对应的地区");
			return result;
		}
		DeviceCityVo location = list.get(0);
//		DeviceCityVo location = new DeviceCityVo();
//		location.setCityId("10284");
//		location.setCityName("北京");
//		location.setCityPinyin("beijing");
//		location.setProvinceName("北京");
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("location", location);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/province")
	public ResultMap allProvince(){
		ResultMap resultMap = new ResultMap();
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		List<Province> provinces = locationService.getAllProvinces();
		resultMap.addContent("provinces", provinces);
		return resultMap;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/city")
	public ResultMap provinceCity(Province province){
		ResultMap resultMap = new ResultMap();
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		List<City> cities = locationService.getProvinceCity(province);
		resultMap.addContent("cities", cities);
		return resultMap;
	}
}
