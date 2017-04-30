package service;

import java.lang.reflect.Field;

import annotation.Command;
import cache.SessionCacheManager;
import model.CleanerStatus;
import model.DeviceInfo;
import model.MCPPacket;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.ByteUtil;
import utils.CRC16;
import utils.Constant;

@Service
public class DeviceControlService {
	
	public static Logger LOG = LoggerFactory.getLogger(DeviceControlService.class);
	
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}

	public <T> boolean statusControl(String command, T input, long deviceID){
		int CTF = Constant.CTF_SET;
		return commandHandler(CTF, command, input, deviceID, CleanerStatus.class);
	}
	
	public <T> boolean statusQuery(String command, T input, long deviceID){
		int CTF = Constant.CTF_QUERY;
		return commandHandler(CTF, command, input, deviceID, CleanerStatus.class);
	}
	public <T> boolean infoControl(String command, T input, long deviceID){
		int CTF = Constant.CTF_SET;
		return commandHandler(CTF, command, input, deviceID, DeviceInfo.class);
	}
	
	public <T> boolean infoQuery(String command, T input, long deviceID){
		int CTF = Constant.CTF_QUERY;
		return commandHandler(CTF, command, input, deviceID, DeviceInfo.class);
	}
	
	public <T> boolean commandHandler(int CTF, String command, T input, long deviceID, Class clazz){
		byte[] data = null;
		Field[] fields = clazz.getDeclaredFields();
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
					data = ByteUtil.intToByteArray((Integer) input, length);
				}else if (input instanceof Long) {
					data = ByteUtil.longToByteArray((Long)input, length);
				}else if (input instanceof String) {
					data = ByteUtil.serverToByte((String) input, length);
				}
				return sentPacket(CTF, cid, deviceID, length, data);
			}
		}
		
		return false;
	}
	
	public boolean setMode(long deviceID, String mode){
		if(mode.equals(Constant.AUTO)){
			
		}else if (mode.equals(Constant.SLEEP)) {
			
		}else if (mode.equals(Constant.MANUAL)) {
			
		}
		
		return false;
	}
	
	
	private boolean sentPacket(int ctfValue, int cidValue, long uidValue, int length, byte[] data){
		byte[] CTF = ByteUtil.intToByteArray(ctfValue, 1);
		byte[] CID = ByteUtil.intToByteArray(cidValue, 1);
		byte[] UID = ByteUtil.longToByteArray(uidValue, 12);
		byte[] LEN = ByteUtil.intToByteArray(length, 1);
		
		byte[] combine = ByteUtil.concatAll(CTF, CID, UID, LEN, data);
		int crcValue = CRC16.CRC_XModem(combine);
		byte[] CRC = ByteUtil.intToByteArray(crcValue, 2);
		MCPPacket packet = new MCPPacket(CTF, CID, UID, LEN, data, CRC);
		
		IoSession session = sessionCacheManager.getSession(uidValue);
		if (session == null) {
			LOG.error("Try to control unknown device : "+ uidValue);
			return false;
		}else if (! session.isConnected()) {
			LOG.error("client has already been disconnected:  "+ uidValue);
			sessionCacheManager.removeSession(uidValue);
			return false;
		}
		try {
			session.write(packet);
		} catch (Exception e) {
			LOG.error("send message failed ! message : " + packet, e);
		}
		return true;
	}
}
