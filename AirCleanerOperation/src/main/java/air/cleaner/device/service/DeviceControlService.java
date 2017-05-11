package air.cleaner.device.service;

import java.lang.reflect.Field;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import air.cleaner.annotation.Command;
import air.cleaner.cache.SessionCacheManager;
import air.cleaner.model.CleanerStatus;
import air.cleaner.model.DeviceInfo;
import air.cleaner.utils.ByteUtil;
import air.cleaner.utils.Constant;
import air.cleaner.utils.PacketSendUtil;

@Service
public class DeviceControlService {
	
	public static Logger LOG = LoggerFactory.getLogger(DeviceControlService.class);
	
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}

	/**
	 * control cleaner status, like Power, velocity, heat, UV
	 * @param command
	 * @param input
	 * @param deviceID
	 * @return
	 */
	public <T> boolean statusControl(String command, T input, String deviceID){
		int CTF = Constant.CTF_SET;
		return commandHandler(CTF, command, input, deviceID, CleanerStatus.class);
	}
	
	/**
	 * query cleaner status
	 * @param command
	 * @param input
	 * @param deviceID
	 * @return
	 */
	public <T> boolean statusQuery(String command, T input, String deviceID){
		int CTF = Constant.CTF_QUERY;
		return commandHandler(CTF, command, input, deviceID, CleanerStatus.class);
	}
	
	/**
	 * control device info, like server, port, heartbeat
	 * @param command
	 * @param input
	 * @param deviceID
	 * @return
	 */
	public <T> boolean infoControl(String command, T input, String deviceID){
		int CTF = Constant.CTF_SET;
		return commandHandler(CTF, command, input, deviceID, DeviceInfo.class);
	}
	
	/**
	 * query device info
	 * @param command
	 * @param input
	 * @param deviceID
	 * @return
	 */
	public <T> boolean infoQuery(String command, T input, String deviceID){
		int CTF = Constant.CTF_QUERY;
		return commandHandler(CTF, command, input, deviceID, DeviceInfo.class);
	}
	
	/**
	 * basic method for command handle
	 * @param CTF
	 * @param command
	 * @param input
	 * @param deviceID
	 * @param clazz
	 * @return
	 */
	public <T> boolean commandHandler(int CTF, String command, T input, String deviceID, Class clazz){
		byte[] data = null;
		Field[] fields = clazz.getDeclaredFields();
		
		IoSession session = sessionCacheManager.getSession(deviceID);
		if (session == null) {
			LOG.error("device has already been disconnected. " + deviceID);
			return false;
		}
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command anno = field.getAnnotation(Command.class);
				String name = anno.name();
				if (! name.equals(command)) {
					continue;
				}
				int length = anno.length();
				int cid = anno.id();
				
				if (input instanceof Integer) {
					data = ByteUtil.intToByteArray((Integer)input, length);
				}else if (input instanceof Long) {
					data = ByteUtil.longToByteArray((Long)input, length);
				}else if (input instanceof String) {
					data = ByteUtil.serverToByte((String) input, length);
				}
				return PacketSendUtil.sentPacket(session, CTF, cid, deviceID, length, data);
			}
		}
		
		return false;
	}
	
	public boolean restart(String deviceID){
		IoSession session = sessionCacheManager.getSession(deviceID);
		if (session == null) {
			LOG.error("device has already been disconnected. " + deviceID);
			return false;
		}
		return PacketSendUtil.sentPacket(session, 0x01, 0x0B, deviceID, 1, new byte[]{0x00});
	}
	
	/**
	 * set mode
	 * @param deviceID
	 * @param mode
	 * @return
	 */
	public boolean setMode(String deviceID, String mode){
		if(mode.equals(Constant.AUTO)){
			
		}else if (mode.equals(Constant.SLEEP)) {
			boolean light = statusControl(Constant.LIGHT, 0, deviceID);
			boolean velocity = statusControl(Constant.VELOCITY, 100, deviceID);
			return light&velocity;
		}else if (mode.equals(Constant.MANUAL)) {
			
		}
		
		return false;
	}
}
