package air.cleaner.mina;

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
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
