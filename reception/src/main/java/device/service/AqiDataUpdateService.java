package device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.AqiCrawler;
import utils.TimeUtil;

@Service
public class AqiDataUpdateService {
	@Autowired
	AqiCrawler aqiCrawler;
	
	public void updateCityAQI(){
		String currentTime = TimeUtil.getCurrentTime();
		aqiCrawler.crawlAQI(currentTime);
	}
}
