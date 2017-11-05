package config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasurementConfig {
	private Logger logger = LoggerFactory.getLogger(MeasurementConfig.class);

	private static Properties props = new Properties();

	static {
		InputStream input = MeasurementConfig.class.getClassLoader().getResourceAsStream("measurement.properties");
		try {
			props.load(input);
		} catch (Exception e) {

		}
	}

	private MeasurementConfig() {
		super();
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}
}
