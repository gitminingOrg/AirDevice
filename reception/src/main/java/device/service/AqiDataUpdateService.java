package device.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bean.DeviceAir;
import dao.DeviceStatusDao;
import util.AqiCrawler;
import utils.TimeUtil;

@Service
public class AqiDataUpdateService {
	@Autowired
	AqiCrawler aqiCrawler;
	@Autowired
	DeviceStatusDao deviceStatusDao;
	
	public void updateCityAQI(){
		String currentTime = TimeUtil.getCurrentTime();
		aqiCrawler.crawlAQI(currentTime);
	}
	
	public void updateDeviceAir(){
		System.out.println("sadsadas");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(date);
		
		HashMap<String, Integer> insideMap = deviceStatusDao.getAverageInside(time + "%");
		HashMap<String, Integer> outsideMap = deviceStatusDao.getAverageOutside(time + "%");
		
		for(String device_id : insideMap.keySet()){
			DeviceAir deviceAir = new DeviceAir();
			deviceAir.setDeviceID(device_id);
			deviceAir.setInsideAir(insideMap.get(device_id));
			deviceAir.setOutsideAir(outsideMap.get(device_id));
			deviceAir.setDate(time);
			deviceStatusDao.insertDeviceAir(deviceAir);
		}
	}
}
