package device.service;

import org.springframework.stereotype.Service;

import util.AqiCrawler;
import utils.TimeUtil;

@Service
public class AqiDataUpdateService {
	public void updateCityAQI(){
		AqiCrawler aqiCrawler = new AqiCrawler();
		String currentTime = TimeUtil.getCurrentTime();
		aqiCrawler.crawlAQI(currentTime);
	}
}
