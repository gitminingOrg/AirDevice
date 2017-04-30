package cache;

import model.CleanerStatus;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class CleanerStatusCacheManager {
    public static Logger LOG = LoggerFactory.getLogger(CleanerStatusCacheManager.class);

    @Autowired
    private MemcachedClient memcachedClient;

    /**
     * get cleaner status
     *
     * @param cleanerID
     * @return
     */
    public CleanerStatus getCleanerStatus(long cleanerID) {
        String key = "status." + cleanerID;
        CleanerStatus status = (CleanerStatus) memcachedClient.get(key);
        return status;
    }

    /**
     * update cleaner status in memcached
     *
     * @param cleanerStatus
     * @return
     */
    public boolean updateCleanerStatus(CleanerStatus cleanerStatus) {
        long deviceID = cleanerStatus.getDeviceID();
        if (deviceID < 0) {
            return false;
        }
        String key = "status." + deviceID;
        LOG.debug("UPDATE STATUS : " + key);
        if (memcachedClient.get(key) == null) {
            memcachedClient.add(key, 0, cleanerStatus);
        } else {
            memcachedClient.replace(key, 0, cleanerStatus);
        }

        return true;
    }
}
