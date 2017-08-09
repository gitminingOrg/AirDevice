package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import config.ReceptionConfig;

public class WechatUtil {
	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	public static String queryAccessToken(String appid, String secret) {
		String result = "";
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ appid + "&secret=" + secret;
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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
			ReceptionConfig.setJsapiTicket(queryJsApiTicket(result));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String queryOauthOpenId(String appid, String secret,
			String code) {
		String result = "";
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appid + "&secret=" + secret + "&code=" + code
				+ "&grant_type=authorization_code";
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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
			result = object.getString("openid");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return result;
	}

	public static String queryJsApiTicket(String token) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ token + "&type=jsapi";
		String result = "";
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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
			result = object.getString("ticket");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return result;
	}

	public static final String inputStream2String(InputStream in)
			throws IOException {
		if (in == null) {
			return "";
		} else {
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];

			int n;
			while ((n = in.read(b)) != -1) {
				out.append(new String(b, 0, n, "UTF-8"));
			}
			return out.toString();
		}
	}

	public static boolean isWechat(HttpServletRequest request) {
		logger.info("user-agent     ccccccccccc  "
				+ request.getHeader("user-agent").toLowerCase());
		if (request.getHeader("user-agent").toLowerCase()
				.contains("micromessenger"))
			return true;
		return false;
	}

	public static String uploadImage(String token, String imgUrl) {
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
				+ token + "&type=image";
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address
					.openConnection();
			File file = new File(imgUrl);
			if (!file.exists() || !file.isFile()) {
				System.out.println("上传的文件不存在");
			}
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			String BOUNDARY = "----------" + System.currentTimeMillis();

			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			// 请求正文信息
			// 第一部分：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\""
					+ imgUrl + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(
					connection.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();

			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String result = buffer.toString();
			JSONObject object = JSON.parseObject(result);
			if(object.containsKey("media_id")){
				return object.getString("media_id");
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return null;
	}
	
	public static boolean pushImage(String token, String openID, String mediaID){
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
		try {
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			OutputStream os = connection.getOutputStream();
			String picMsg = "{\"touser\":\""+openID+"\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""+mediaID+"\"}}";
			os.write(picMsg.getBytes("UTF-8"));
			connection.connect();
			InputStream is = connection.getInputStream();
			int size = is.available();
			byte[] bytes = new byte[size];
			is.read(bytes);
			String message = new String(bytes, "UTF-8");
			//JSONObject object = JSON.parseObject(message);
			System.err.println(message);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return true;
	}
}
