package air.cleaner.cache;

import model.CleanerStatus;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CleanerStatusCacheManager {
	public static Logger LOG = LoggerFactory.getLogger(CleanerStatusCacheManager.class);
	@Autowired
	private MemcachedClient memcachedClient;
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	/**
	 * get cleaner status 
	 * @param cleanerID
	 * @return
	 */
	public CleanerStatus getCleanerStatus(String cleanerID){
		String key = "status."+cleanerID;
		CleanerStatus status = (CleanerStatus) memcachedClient.get(key);
		return status;
	}
	
	/**
	 * update cleaner status in memcached
	 * @param cleanerStatus
	 * @return
	 */
	public boolean updateCleanerStatus(CleanerStatus cleanerStatus){
		String deviceID = cleanerStatus.getDeviceID();
		if (deviceID == null) {
			return false;
		}
		String key = "status."+deviceID;
		//LOG.info("UPDATE STATUS : " + key);
		if (memcachedClient.get(key) == null) {
			memcachedClient.add(key, 0, cleanerStatus);
		}else{
			memcachedClient.replace(key, 0, cleanerStatus);
		}
		
		return true;
	}
}
