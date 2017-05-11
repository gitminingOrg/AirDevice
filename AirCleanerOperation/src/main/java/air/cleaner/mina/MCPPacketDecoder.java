package air.cleaner.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import air.cleaner.model.MCPPacket;
import air.cleaner.model.HeartbeatMCPPacket;
import air.cleaner.utils.ByteUtil;

public class MCPPacketDecoder extends CumulativeProtocolDecoder {
	public static Logger LOG = LoggerFactory.getLogger(MCPPacketDecoder.class);
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOG.info("start decode");
		//the minimum length of MCP packet should be 19 bytes
		if(in.remaining() >= 19){
			byte[] FRH = new byte[1];
			in.get(FRH);
			
			byte[] CTF = new byte[1];
			in.get(CTF);
			
			byte[] CID = new byte[1];
			in.get(CID);
			
			byte[] UID = new byte[12];
			in.get(UID);
			
			byte[] LEN = new byte[1];
			in.get(LEN);
			
			int length = ByteUtil.byteArrayToInt(LEN);
			
			byte[] DATA = new byte[length];
			if(in.remaining() < length+3){
				//reset position, turn on to the next packet
				LOG.warn("FRH : " + ByteUtil.byteToHex(FRH));
				LOG.warn("CTF : " + ByteUtil.byteToHex(CTF));
				LOG.warn("CID : " + ByteUtil.byteToHex(CID));
				LOG.warn("UID : " + ByteUtil.byteToHex(UID));
				LOG.warn("remain size is " + in.remaining() + ",MCPPacket data module need: " +length);
				return false;
			}
			in.get(DATA);
			
			byte[] CRC = new byte[2];
			in.get(CRC);
			
			byte[] FRT = new byte[1];
			in.get(FRT); 
			
			MCPPacket packet = new MCPPacket(FRH, CTF, CID, UID, LEN, DATA, CRC, FRT);
			if (MCPPacket.validateMCPPacket(packet)) {
				if(HeartbeatMCPPacket.checkHeartbeatMCPPacket(packet)){
					packet = new HeartbeatMCPPacket(FRH, CTF, CID, UID, LEN, DATA, CRC, FRT);
				}
				out.write(packet);
			}
			
			if(in.hasRemaining()){
				return true;
			}
		}else{
			LOG.warn("received packet too short for MCPPacket");
		}
		return false;
	}

}
