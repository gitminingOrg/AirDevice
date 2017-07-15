package config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementConfig {
	private Logger logger = LoggerFactory.getLogger(ManagementConfig.class);
	
	private static Properties props = new Properties();
	
	static {
		InputStream input = ManagementConfig.class.getClassLoader().getResourceAsStream("management.properties");
		try {
			props.load(input);
		} catch (Exception e) {

		}
	}
	
	private ManagementConfig() {
		super();
	}
	
	public static String getValue(String key) {
		return props.getProperty(key);
	}
}
