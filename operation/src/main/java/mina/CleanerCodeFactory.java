package mina;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CleanerCodeFactory implements ProtocolCodecFactory {
	private final MCPPacketEncoder encoder;  
    private final MCPPacketDecoder decoder;
    
    public CleanerCodeFactory() {  
        this(Charset.forName("UTF-8"));  
    }
    
    public CleanerCodeFactory(Charset charset){
    	encoder = new MCPPacketEncoder();
    	decoder = new MCPPacketDecoder();
    }
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

}
