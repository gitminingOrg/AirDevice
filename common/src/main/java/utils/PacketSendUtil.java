package utils;

import model.MCPPacket;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketSendUtil {
	public static Logger LOG = LoggerFactory.getLogger(PacketSendUtil.class);
	
	/**
	 * send packet in form of MCPPacket
	 * @param ctfValue
	 * @param cidValue
	 * @param uidValue
	 * @param length
	 * @param data
	 * @return
	 */
	public static boolean sentPacket(IoSession session, int ctfValue, int cidValue, String uidValue, int length, byte[] data){
		//encapsulate mcppacket
		byte[] CTF = ByteUtil.intToByteArray(ctfValue, 1);
		byte[] CID = ByteUtil.intToByteArray(cidValue, 1);
		byte[] UID = ByteUtil.hexStringByteTo(uidValue, 12);
		byte[] LEN = ByteUtil.intToByteArray(length, 1);
		
		byte[] combine = ByteUtil.concatAll(CTF, CID, UID, LEN, data);
		int crcValue = CRC16.CRC_XModem(combine);
		byte[] CRC = ByteUtil.intToByteArray(crcValue, 2);
		MCPPacket packet = new MCPPacket(CTF, CID, UID, LEN, data, CRC);
		
		//send packet
		try {
			if (session == null) {
				LOG.error("Try to control unknown device : "+ uidValue);
				return false;
			}
			session.write(packet);
		} catch (Exception e) {
			LOG.error("send message failed ! message : " + packet, e);
		}
		return true;
	}
}
