package air.cleaner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	@Value("#{configFile.local}")
	private String localDomain;
	@Value("#{configFile.local}")
	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}
	public String getLocalDomain() {
		return localDomain;
	}

	@Value("#{configFile.notify}")
	private String notifyServer;
	@Value("#{configFile.notify}")
	public void setNotifyServer(String notifyServer) {
		this.notifyServer = notifyServer;
	}
	public String getNotifyServer() {
		return notifyServer;
	}
	
	@Value("#{configFile.notify_port}")
	private String notifyPort;
	public String getNotifyPort() {
		return notifyPort;
	}
	@Value("#{configFile.notify_port}")
	public void setNotifyPort(String notifyPort) {
		this.notifyPort = notifyPort;
	}
	
	@Value("#{configFile.notify_path}")
	private String notifyPath;
	public String getNotifyPath() {
		return notifyPath;
	}
	@Value("#{configFile.notify_path}")
	public void setNotifyPath(String notifyPath) {
		this.notifyPath = notifyPath;
	}
	
	@Value("#{configFile.web_port}")
	private String webPort;
	public String getWebPort() {
		return webPort;
	}
	@Value("#{configFile.web_port}")
	public void setWebPort(String webPort) {
		this.webPort = webPort;
	}
	
}
