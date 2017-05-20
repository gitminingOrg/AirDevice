package util;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AqiCrawler {
	Logger LOG = LoggerFactory.getLogger(AqiCrawler.class);
	
	public void crawlAQI(String time){
		AqiDao aqiDao = new AqiDao();
		HashMap<String, String> city_map = aqiDao.getCityList();
		
		try {
			for(String city_name : city_map.keySet()){
				String city_pinyin = city_map.get(city_name);
				Document detail_doc = Jsoup.connect("http://air-level.com/air/" + city_pinyin).timeout(90000).get();
				//some cities have no aqi data,the webpage may redirect,need skip
				if(detail_doc.getElementsByClass("aqi-bg").size() > 0){
					Element data_element = detail_doc.getElementsByClass("aqi-bg").get(0);
					int aqi_value = Integer.parseInt(data_element.html().split(" ")[0]);
					String aqi_grade = data_element.html().split(" ")[1];
					System.out.println(city_name + ":" + data_element.html());
					//each hour period can only have one aqi record
					if(!aqiDao.findCityAqi(city_name, time)){
						aqiDao.insertCityAqi(city_name, city_pinyin, time, aqi_value, aqi_grade);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Crawler Error", e);
		}
	}
	
	public void updateCityList(){
		AqiDao aqiDao = new AqiDao();
		
		try {
			Document doc = Jsoup.connect("http://air-level.com/").timeout(90000).get();
			Elements cityNames = doc.getElementsByClass("citynames");
			for(int i = 1 ; i < cityNames.size() ; i ++){
				Elements cities = cityNames.get(i).children();
				for(Element city : cities){
					String city_name = city.html();
					String city_pinyin = city.attr("href").split("/")[2];
					String initial = (city_pinyin.charAt(0) + "").toUpperCase();
					
					if(!aqiDao.findCity(city_name)){
						aqiDao.insertCity(city_name, city_pinyin, initial);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Crawler Error", e);
		}
	}
}
