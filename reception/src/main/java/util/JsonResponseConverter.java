package util;

import com.google.gson.Gson;

import model.ResultMap;
import utils.HttpDeal;


public class JsonResponseConverter {
	public static ResultMap getResultMap(String domain, String port, String path){
		return getResultMapWithParams(domain, port, path);
	}
	
	/**
	 * use default domain & port
	 * @param path
	 * @param params
	 * @return
	 */
	public static ResultMap getDefaultResultMapWithParams(String path, String... params){
		return getResultMapWithParams(ReceptionConstant.domain, ReceptionConstant.port, path, params);
	}
	
	public static ResultMap getResultMapWithParams(String domain, String port, String path, String... params){
		String urlTemplate = "http://"+domain+":"+ port + path ;
		String url = urlTemplate;
		if (params.length > 0) {
			url = String.format(urlTemplate, params);
		}
		String result = HttpDeal.getResponse(url);
		Gson gson = new Gson();
		ResultMap resultMap = gson.fromJson(result, ResultMap.class);
		return resultMap;
	}
}
