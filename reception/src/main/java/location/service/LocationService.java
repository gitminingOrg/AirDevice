package location.service;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import config.ReceptionConfig;

@Service
public class LocationService {
	private Logger logger = LoggerFactory.getLogger(LocationService.class);
	
	public void province() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet("http://apis.map.qq.com/ws/district/v1/list?key=" + ReceptionConfig.getValue("map_key"));
		CloseableHttpResponse response = null;
		try {
			response = (CloseableHttpResponse) client.execute(request);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				JSONObject json = JSONObject.parseObject(result);
				logger.info(json.toJSONString());
			}
			logger.error(response.getStatusLine().getStatusCode() + "获取接口数据失败");
		}catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			try {
				response.close();
				client.close();
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
}
