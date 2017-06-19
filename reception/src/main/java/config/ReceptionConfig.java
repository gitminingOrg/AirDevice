package config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import util.WechatUtil;
@Component
public class ReceptionConfig {
	private Logger logger = LoggerFactory.getLogger(ReceptionConfig.class);
	
	private static String accessToken;

	private static String jsapiTicket;

	private static Properties props = new Properties();

	static {
		InputStream input = ReceptionConfig.class.getClassLoader().getResourceAsStream("reception.properties");
		try {
			props.load(input);
		} catch (Exception e) {

		}
	}

	private ReceptionConfig() {
		super();
	}

	public static String getAccessToken() {
		if (StringUtils.isEmpty(accessToken)) {
			accessToken = WechatUtil.queryAccessToken(ReceptionConfig.getValue("wechat_appid"),
					ReceptionConfig.getValue("wechat_secret"));
		}
		return accessToken;
	}

	public static String getJsapiTicket() {
		if (StringUtils.isEmpty(jsapiTicket)) {
			getAccessToken();
		}
		return jsapiTicket;
	}

	public static void setJsapiTicket(String jsapiTicket) {
		ReceptionConfig.jsapiTicket = jsapiTicket;
	}

	public static void setAccessToken(String token) {
		accessToken = token;
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public void schedule() {
		accessToken = WechatUtil.queryAccessToken(ReceptionConfig.getValue("wechat_appid"), ReceptionConfig.getValue("wechat_secret"));
	}
}
