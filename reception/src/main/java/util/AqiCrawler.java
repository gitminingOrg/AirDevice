package util;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bean.CityAqi;
import bean.CityList;
import dao.DeviceStatusDao;

@Component
public class AqiCrawler {
	@Autowired
	private DeviceStatusDao deviceStatusDao;
	
	Logger LOG = LoggerFactory.getLogger(AqiCrawler.class);
	
	public void crawlAQI(String time){
		List<CityList> cityList = deviceStatusDao.getAllCities();
		
		try {
			for(int i = 0 ; i < cityList.size() ; i ++) {
				String city_pinyin = cityList.get(i).getCityPinyin();
				Document detail_doc = Jsoup.connect("http://air-level.com/air/" + city_pinyin).timeout(90000).get();
				//some cities have no aqi data,the webpage may redirect,need skip
				if(detail_doc.getElementsByClass("aqi-bg").size() > 0){
					Element data_element = detail_doc.getElementsByClass("aqi-bg").get(0);
					int aqi_value = Integer.parseInt(data_element.html().split(" ")[0]);
					String aqi_grade = data_element.html().split(" ")[1];
					System.out.println(cityList.get(i).getCityName() + ":" + data_element.html());
					//each hour period can only have one aqi record
					if(!deviceStatusDao.findCityAqi(cityList.get(i).getCityName(), time)){
						CityAqi cityAqi = new CityAqi();
						cityAqi.setCityName(cityList.get(i).getCityName());
						cityAqi.setCityPinyin(city_pinyin);
						cityAqi.setTime(time);
						cityAqi.setAqiData(aqi_value);
						cityAqi.setAqiGrade(aqi_grade);
						deviceStatusDao.insertCityAqi(cityAqi);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Crawler Error", e);
		}
	}
	
	public void updateCityList(){
		
		try {
			Document doc = Jsoup.connect("http://air-level.com/").timeout(90000).get();
			Elements cityNames = doc.getElementsByClass("citynames");
			for(int i = 1 ; i < cityNames.size() ; i ++){
				Elements cities = cityNames.get(i).children();
				for(Element city : cities){
					String city_name = city.html();
					String city_pinyin = city.attr("href").split("/")[2];
					String initial = (city_pinyin.charAt(0) + "").toUpperCase();
					
					if(!deviceStatusDao.findCity(city_name)){
						CityList cityList = new CityList();
						cityList.setCityName(city_name);
						cityList.setCityPinyin(city_pinyin);
						cityList.setInitial(initial);
						deviceStatusDao.insertCity(cityList);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Crawler Error", e);
		}
	}
}
