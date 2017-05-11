package air.cleaner.cache;

import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import air.cleaner.model.DeviceInfo;
import air.cleaner.utils.TimeUtil;

@Repository
public class DeviceInfoCacheManager {
	public static final String DEVICE_ = "device."; 
	@Autowired
	private MemcachedClient memcachedClient;
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public DeviceInfo getDeviceInfo(String deviceID){
		String key = DEVICE_+deviceID;
		DeviceInfo deviceInfo = (DeviceInfo) memcachedClient.get(key);
		return deviceInfo;
	}
	
	public boolean updateDevice(DeviceInfo deviceInfo){
		if (deviceInfo.getDeviceID() == null) {
			return false;
		}
		String key = DEVICE_+deviceInfo.getDeviceID();
		deviceInfo.setUpdateTime(TimeUtil.getCurrentTime());
		if (memcachedClient.get(key) == null) {
			memcachedClient.add(key, 0, deviceInfo);
		}else{
			memcachedClient.replace(key, 0, deviceInfo);
		}
		return true;
	}
}
