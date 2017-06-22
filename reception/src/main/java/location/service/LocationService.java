package location.service;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import config.ReceptionConfig;
import dao.LocationDao;
import model.location.City;
import model.location.Province;

@Service
public class LocationService {
	private Logger logger = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private LocationDao locationDao;

	public void init() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(
				"http://apis.map.qq.com/ws/district/v1/list?key=" + ReceptionConfig.getValue("map_key"));
		CloseableHttpResponse response = null;
		try {
			response = (CloseableHttpResponse) client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				JSONObject json = JSONObject.parseObject(result);
				JSONArray list = json.getJSONArray("result");
				JSONArray provinces = list.getJSONArray(0);
				JSONArray cities = list.getJSONArray(1);
				province(provinces, cities);
			} else {
				logger.error(response.getStatusLine().getStatusCode() + "获取接口数据失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				response.close();
				client.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * list data format: [{"pinyin" : xxx, "cidx": [m, n], "name": xxx, "id":
	 * xxx, "fullname": xxx}...]
	 *
	 * @param provinces
	 */
	private void province(JSONArray provinces, JSONArray cities) {
		for (int i = 0; i < provinces.size(); i++) {
			JSONObject current = provinces.getJSONObject(i);
			String temp = current.getString("pinyin");
			String pinyin = pinyin(temp);
			String name = current.getString("name");
			String id = current.getString("id");
			String fullname = current.getString("fullname");
			Province province = new Province(id, fullname, name, pinyin);
			boolean status = locationDao.insertProvince(province);
			logger.info("插入" + fullname + "数据" + ((status == true) ? "成功" : "失败"));
			int start = current.getJSONArray("cidx").getIntValue(0);
			int end = current.getJSONArray("cidx").getIntValue(1);
			city(province, cities, start, end);
		}
	}

	/**
	 * list data format: [{"id": xxx, "name": xxx, "fullname": xxx, "pinyin":
	 * xxx }...]
	 * 
	 * @param list
	 */
	private void city(Province province, JSONArray list, int start, int end) {
		for (int i = start; i <= end; i++) {
			JSONObject current = list.getJSONObject(i);
			String temp = current.getString("pinyin");
			String pinyin = pinyin(temp);
			String name = current.getString("name");
			String id = current.getString("id");
			String fullname = current.getString("fullname");
			City city = new City(province, id, fullname, name, pinyin);
			boolean status = locationDao.insertCity(city);
			logger.info("插入" + fullname + "数据" + ((status == true) ? "成功" : "失败"));
		}
	}

	private String pinyin(String input) {
		String pinyin = input.substring(1, input.length() - 1).replaceAll("\"", "").replaceAll(",", "");
		return pinyin;
	}
}
