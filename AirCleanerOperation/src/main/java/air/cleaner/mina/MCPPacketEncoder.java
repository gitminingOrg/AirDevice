package air.cleaner.mina;

import model.MCPPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCPPacketEncoder extends ProtocolEncoderAdapter {
	public static Logger LOG = LoggerFactory.getLogger(MCPPacketEncoder.class);
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		 
		if(message instanceof MCPPacket){
			IoBuffer buffer = IoBuffer.allocate(19).setAutoExpand(true);
			byte[] items = ((MCPPacket) message).toByte();
			buffer.put(items);
			buffer.flip();
			LOG.info("encode mcppacket" + message);
			out.write(buffer);
			out.flush();
		}else{
			LOG.error("encode mcppacket failed" + message);
		}
		
	}

}
