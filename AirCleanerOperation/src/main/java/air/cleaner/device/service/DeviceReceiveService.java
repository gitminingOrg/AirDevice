package air.cleaner.device.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import model.CleanerStatus;
import model.DeviceInfo;
import model.HeartbeatMCPPacket;
import model.MCPPacket;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.ByteUtil;
import utils.Constant;
import utils.MethodUtil;
import utils.PacketSendUtil;
import utils.TimeUtil;
import air.cleaner.cache.CleanerStatusCacheManager;
import air.cleaner.cache.DeviceInfoCacheManager;
import air.cleaner.cache.SessionCacheManager;
import annotation.Command;

@Service
public class DeviceReceiveService {
	public static Logger LOG = LoggerFactory.getLogger(DeviceReceiveService.class);
	
	@Autowired
	private CleanerStatusCacheManager cleanerStatusCacheManager;
	
	public void setCleanerStatusCacheManager(
			CleanerStatusCacheManager cleanerStatusCacheManager) {
		this.cleanerStatusCacheManager = cleanerStatusCacheManager;
	}
	
	@Autowired
	private DeviceInfoCacheManager deviceInfoCacheManager;
	public void setDeviceInfoCacheManager(
			DeviceInfoCacheManager deviceInfoCacheManager) {
		this.deviceInfoCacheManager = deviceInfoCacheManager;
	}
	
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	
	@Autowired
	private DeviceControlService deviceControlService;
	public void setDeviceControlService(DeviceControlService deviceControlService) {
		this.deviceControlService = deviceControlService;
	}

	/**
	 * update cleaner status in cache
	 * @param packet MCPPacket from device
	 */
	public void updateCacheCleanerStatus(HeartbeatMCPPacket packet, String ip){
		CleanerStatus cleanerStatus = packet.packetToCleanerStatus();
		cleanerStatus.setIp(ip);
		String time = TimeUtil.getCurrentTime();
		cleanerStatus.setTime(time);
		boolean update = cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
		if (!update) {
			LOG.warn("update cleaner status cache failed");
		}else {
			//LOG.info("update succeed");
		}
	}
	
	/**
	 * update device info in cache
	 * @param packet
	 */
	public void updateCacheDeviceInfo(MCPPacket packet){
		int command = ByteUtil.byteArrayToInt(packet.getCID());
		String deviceID = ByteUtil.byteToHex(packet.getUID());
		DeviceInfo deviceInfo = deviceInfoCacheManager.getDeviceInfo(deviceID);
		
		if (deviceInfo == null) {
			//////init device
			deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceID(deviceID);
		}
		Field[] fields = DeviceInfo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command anno = field.getAnnotation(Command.class);
				if (anno.id() == command) {
					String methodName = MethodUtil.setFieldGetMethod(anno.name());
					if (field.getGenericType().getTypeName().equals("int")) {
						int value = ByteUtil.byteArrayToInt(packet.getDATA());
						try {
							Method method = deviceInfo.getClass().getDeclaredMethod(methodName, int.class);
							method.invoke(deviceInfo, value);
							deviceInfoCacheManager.updateDevice(deviceInfo);
						} catch (Exception e) {
							LOG.error("no such set method <int>" + methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("java.lang.String")) {
						String value = ByteUtil.byteToServer(packet.getDATA());
						try {
							Method method = deviceInfo.getClass().getDeclaredMethod(methodName, String.class);
							method.invoke(deviceInfo, value);
							deviceInfoCacheManager.updateDevice(deviceInfo);
						} catch (Exception e) {
							LOG.error("no such set method <String>" + methodName, e);
						}
					}
					break;
				}
			}
		}
	}
	
	/**
	 * update a single attribute among cleaner status
	 * @param packet
	 */
	public void updateSingleCacheCleanerStatus(MCPPacket packet){
		int command = ByteUtil.byteArrayToInt(packet.getCID());
		LOG.info("控制指令为："+command);
		String deviceID = ByteUtil.byteToHex(packet.getUID());
		CleanerStatus cleanerStatus = cleanerStatusCacheManager.getCleanerStatus(deviceID);
		
		if (cleanerStatus == null) {
			//////init status
		}
		Field[] fields = CleanerStatus.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command anno = field.getAnnotation(Command.class);
				if (anno.id() == command) {
					String methodName = MethodUtil.setFieldGetMethod(anno.name());
					if (field.getGenericType().getTypeName().equals("int")) {
						int value = ByteUtil.byteArrayToInt(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, int.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <int>" + methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("java.lang.String")) {
						String value = ByteUtil.byteToServer(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, String.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <String>" +methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("long")) {
						long value = ByteUtil.byteArrayToLong(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, long.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <long>" +methodName, e);
						}
					}
					break;
				}
			}
		}
		String time = TimeUtil.getCurrentTime();
		cleanerStatus.setTime(time);
	}
	
	public CleanerStatus getCleanerStatus(String deviceID){
		return cleanerStatusCacheManager.getCleanerStatus(deviceID);
	}
	
	/**
	 * get device info from cache... attention ! first update!
	 * @param deviceID
	 * @return
	 */
	public DeviceInfo getDeviceInfo(String deviceID){
		boolean update = updateDeviceInfo(deviceID);
		return deviceInfoCacheManager.getDeviceInfo(deviceID);
	}
	
	private boolean updateDeviceInfo(String deviceID){
		boolean result = true;
		IoSession session = sessionCacheManager.getSession(deviceID);
		if (session == null) {
			LOG.error("device has already been disconnected! " + deviceID);
		}
		Field[] fields = DeviceInfo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command command = field.getAnnotation(Command.class);
				int ctf = Constant.CTF_QUERY;
				int cid = command.id();
				String uid = deviceID;
				int len = command.length();
				byte[] data = new byte[len];
				result = PacketSendUtil.sentPacket(session, ctf, cid, uid, len, data) && result;
			}
		}
		if(!result){
			LOG.warn("update device info failed!");
		}
		return result;
	}
	
	public boolean checkSessionAlive(String deviceID){
		IoSession session = sessionCacheManager.getSession(deviceID);
		if(session == null || !session.isConnected()){
			return false;
		}
		return true;
	}
}
