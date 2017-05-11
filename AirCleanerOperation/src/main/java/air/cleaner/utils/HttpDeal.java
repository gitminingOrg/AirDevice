package air.cleaner.utils;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpDeal {
	public static String getResponse(String url){
		try{
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(new URI(url));
			HttpResponse response = httpClient.execute(get);
			String userJsons = EntityUtils.toString(response.getEntity());
			return userJsons;			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
