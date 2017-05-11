package air.cleaner.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import air.cleaner.annotation.AQIData;
import air.cleaner.utils.ByteUtil;
import air.cleaner.utils.MethodUtil;

/**
 * this is the heartbeat form-like MCPPacket, specialized for AQI heartbeat data
 * @author owenchen
 *
 */
public class HeartbeatMCPPacket extends MCPPacket implements Serializable{
	private static final long serialVersionUID = -5164127950034302166L;

	public HeartbeatMCPPacket(byte[] fRH, byte[] cTF, byte[] cID, byte[] uID,
			byte[] lEN, byte[] dATA, byte[] cRC, byte[] fRT) {
		super(fRH, cTF, cID, uID, lEN, dATA, cRC, fRT);
	}

	public CleanerStatus packetToCleanerStatus(){
		CleanerStatus cleanerStatus = new CleanerStatus();
		String deviceID = ByteUtil.byteToHex(getUID());
		cleanerStatus.setDeviceID(deviceID);
		
		byte[] data = getDATA();
		Field[] fields = cleanerStatus.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(AQIData.class)){
				AQIData aqiData = field.getAnnotation(AQIData.class);
				//find value in byte
				int start = aqiData.start();
				int length = aqiData.length();
				//identify field name
				String name = aqiData.name();
				String type = field.getGenericType().getTypeName();
				byte[] valueArray = Arrays.copyOfRange(data, start, start+length);
				String setName = MethodUtil.setFieldGetMethod(name);
				
				try {
					if(type.equals("int")){
						Method setMethod = cleanerStatus.getClass().getDeclaredMethod(setName, int.class);
						int value = ByteUtil.byteArrayToInt(valueArray);
						setMethod.invoke(cleanerStatus, value);
					}else if(type.equals("long")){
						Method setMethod = cleanerStatus.getClass().getDeclaredMethod(setName, long.class);
						long value = ByteUtil.byteArrayToLong(valueArray);
						setMethod.invoke(cleanerStatus, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return cleanerStatus;
	}
	
	public static boolean checkHeartbeatMCPPacket(MCPPacket packet){
		if(ByteUtil.byteArrayToInt(packet.getCID()) != 0x00 ){
			return false;
		}
		
		if(packet.getDATA().length != 32){
			LOG.info("heartbeat packet length incorrect, need 32, actual " + packet.getDATA().length);
			return false;
		}
		
		return true;
	}
}
