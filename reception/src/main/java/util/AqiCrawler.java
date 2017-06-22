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
			Document detail_doc = Jsoup.connect("http://pm25.in/rank").timeout(90000).get();
			Elements data_element = detail_doc.getElementsByTag("tr");
			for(int i = 1 ; i < data_element.size() ; i ++){
				Elements tdElements = data_element.get(i).children();
				String city_name = tdElements.get(1).html().split(">")[1].split("<")[0];
				String city_pinyin = tdElements.get(1).html().split("/")[1].split("\"")[0];
				int city_aqi = Integer.parseInt(tdElements.get(2).html());
				String aqi_category = tdElements.get(3).html();
				String primary_pollutant = tdElements.get(4).html();
				int pm25 = tdElements.get(5).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(5).html());
				int pm10 = tdElements.get(6).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(6).html());
				double co = tdElements.get(7).html().equals("_") ? 0.0 : Double.parseDouble(tdElements.get(7).html());
				int no2 = tdElements.get(8).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(8).html());
				int o3 = tdElements.get(9).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(9).html());
				int o3_8h = tdElements.get(10).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(10).html());
				int so2 = tdElements.get(11).html().equals("_") ? 0 : Integer.parseInt(tdElements.get(11).html());
				
				CityAqi cityAqi = new CityAqi();
				cityAqi.setCityName(city_name);
				cityAqi.setCityPinyin(city_pinyin);
				cityAqi.setTime(time);
				cityAqi.setCityAqi(city_aqi);
				cityAqi.setAqiCategory(aqi_category);
				cityAqi.setPrimaryPollutant(primary_pollutant);
				cityAqi.setPm25(pm25);
				cityAqi.setPm10(pm10);
				cityAqi.setCo(co);
				cityAqi.setNo2(no2);
				cityAqi.setO3(o3);
				cityAqi.setO3_8h(o3_8h);
				cityAqi.setSo2(so2);
				
				if(!deviceStatusDao.findCityAqi(city_name, time)){
					deviceStatusDao.insertCityAqi(cityAqi);
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
