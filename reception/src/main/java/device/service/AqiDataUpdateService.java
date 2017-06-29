package device.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import location.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.AqiCrawler;
import utils.TimeUtil;
import vo.location.DeviceCityVo;
import bean.DeviceAir;
import bean.DeviceCity;
import dao.DeviceStatusDao;
import dao.DeviceVipDao;

@Service
public class AqiDataUpdateService {
	@Autowired
	AqiCrawler aqiCrawler;
	@Autowired
	DeviceStatusDao deviceStatusDao;
	@Autowired
	DeviceVipDao deviceVipDao;
	@Autowired
	LocationService locationService;
	
	public void updateCityAQI(){
		String currentTime = TimeUtil.getCurrentTime();
		aqiCrawler.crawlAQI(currentTime);
	}
	
	public void updateDeviceAir(){
		//update device cities
		updateDeviceCity();
		
		//get last date
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(date);
		
		HashMap<String, Integer> insideMap = deviceStatusDao.getAverageInside(time + "%");
		HashMap<String, Integer> outsideMap = deviceStatusDao.getAverageOutside(time + "%");
		//pair device air & city air
		for(String device_id : insideMap.keySet()){
			DeviceAir deviceAir = new DeviceAir();
			deviceAir.setDeviceID(device_id);
			deviceAir.setInsideAir(insideMap.get(device_id));
			if(!outsideMap.containsKey(device_id)){
				continue;
			}
			deviceAir.setOutsideAir(outsideMap.get(device_id));
			deviceAir.setDate(time);
			deviceStatusDao.insertDeviceAir(deviceAir);
		}
	}
	
	public void updateDeviceCity(){
		List<String> devideIds = deviceStatusDao.selectAllActiveDevices();
		List<DeviceCity> deviceCities = new ArrayList<DeviceCity>();
		for (String deviceId : devideIds) {
			DeviceCityVo vo = locationService.getDeviceLocation(deviceId);
			if(vo != null){
				DeviceCity deviceCity = new DeviceCity(deviceId, vo.getCityPinyin(), 1);
				deviceCities.add(deviceCity);
			}
		}
		deviceStatusDao.disableAllDiviceCity();
		deviceStatusDao.inserDiviceCityList(deviceCities);
	}
}
