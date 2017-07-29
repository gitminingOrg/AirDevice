package menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MenuConfig {
	private final static String wechat_appid = "wxe8ca0dc55e5f81fd";
	private final static String wechat_secret = "f059ce8503d18423654d6c1d7a8129e2";

	/**
	 * 调用此方法需传入appid和secret，现默认不使用该方法获取
	 *
	 * @param appId
	 * @param secret
	 * @return
	 */
	public static String queryAccessToken(String appId, String secret) {
		String result = "";
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret="
				+ secret;
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			connection.connect();
			InputStream is = connection.getInputStream();
			int size = is.available();
			byte[] bytes = new byte[size];
			is.read(bytes);
			String message = new String(bytes, "UTF-8");
			JSONObject object = JSON.parseObject(message);
			result = object.getString("access_token");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String createMenu(String token) {
		//设备块
		JSONObject mine_device = new JSONObject();
		mine_device.put("name", "我的设备");
		mine_device.put("type", "view");
		try {
			mine_device.put("url", "http://commander.qingair.net/reception/www/index.html#/home/device");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject new_device = new JSONObject();
		new_device.put("name", "发现设备");
		new_device.put("type", "scancode_push");
		new_device.put("key", "discover");
		
		JSONArray device_button = new JSONArray();
		device_button.add(mine_device);
		device_button.add(new_device);
		
		JSONObject deivce = new JSONObject();
		deivce.put("name", "设备");
		deivce.put("sub_button", device_button);
		
		//会员块
		JSONObject vip_bonus = new JSONObject();
		mine_device.put("name", "积分商城");
		mine_device.put("type", "view");
		try {
			mine_device.put("url", "http://commander.qingair.net/reception/www/index.html#/home/device");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject vip_code = new JSONObject();
		new_device.put("name", "优惠码");
		new_device.put("type", "scancode_push");
		new_device.put("key", "discover");
		
		JSONObject vip = new JSONObject();
		vip.put("name", "我的");
		vip.put("type", "view");
		try {
			vip.put("url", "http://commander.qingair.net/reception/www/index.html");
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		JSONObject about = new JSONObject();
		about.put("name", "关于产品");
		about.put("type", "view");
		try {
			about.put("url", "http://commander.qingair.net/reception/www/index.html");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray buttons = new JSONArray();
		buttons.add(deivce);
		buttons.add(vip);
		buttons.add(about);
		JSONObject menu = new JSONObject();
		menu.put("button", buttons);
		System.out.println(JSON.toJSONString(menu));
		String link = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(menu.toString().getBytes());
            os.flush();
            os.close();

            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            return "返回信息" + message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "创建菜单失败";
		
		
	}
	
	   public static String deleteMenu(String token) {
	        String link = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
	        try {
	            URL url = new URL(link);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
	            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	            connection.connect();
	            OutputStream os = connection.getOutputStream();
	            os.flush();
	            os.close();
	            InputStream is = connection.getInputStream();
	            int size = is.available();
	            byte[] bytes = new byte[size];
	            is.read(bytes);
	            String message = new String(bytes, "UTF-8");
	            return "返回信息:" + message;
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return "删除菜单失败";
	    }
	
    public static void main(String[] args) {
        String token = "RWto3tIMnPKREXxpff08go7DxYXyDclmJMat2Qbb7BbOpWTIe6WgjskDu2Y1jk7YKZIOaRgbi2mvTe7udxNWiuPL7Ba6nObWiRxgCHndw1s7JEUolUfYDzhdfwQQPhKCMOXaAJAFHQ";
        String deleteMessage = MenuConfig.deleteMenu(token);
        System.out.println("删除操作: " + deleteMessage);
        String createMessage = MenuConfig.createMenu(token);
        System.out.println("创建操作" + createMessage);
    }
}
