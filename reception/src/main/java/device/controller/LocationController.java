package device.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("location", location);
		return result;
	}
}
