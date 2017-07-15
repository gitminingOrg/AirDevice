package air.cleaner.cache;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SessionCacheManager {
	public static final String SESSION_ = "session."; 
	public static Logger LOG = LoggerFactory.getLogger(SessionCacheManager.class);
	
	private ExpiringMap<String, IoSession> sessionMap = new ExpiringMap<String, IoSession>(10*60);
	@PostConstruct
	public void init(){
		sessionMap.getExpirer().startExpiring();
	}

	public IoSession getSession(String deviceID){
		String key = SESSION_+deviceID;
		IoSession session = (IoSession) sessionMap.get(key);
		if (session == null) {
			return null;
		}else if (! session.isConnected() || session.isClosing()) {
			LOG.error("client has already been disconnected:  "+ deviceID);
			removeSession(deviceID);
			return null;
		}
		return session;
	}
	
	public boolean updateSession(String deviceID, IoSession session){
		String key = SESSION_+deviceID;
		if (sessionMap.get(key) != null) {
			sessionMap.replace(key, session);
		}else{
			sessionMap.put(key, session);
		}
		return true;
	}
	
	public boolean removeSession(String deviceID){
		IoSession session =  sessionMap.remove(deviceID);
		if (session != null) {
			return true;
		}
		return false;
	}
	
	public Set<String> getAllDeviceID(){
		return sessionMap.keySet();
	}
}
