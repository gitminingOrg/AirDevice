package air.cleaner.mina;

import model.HeartbeatMCPPacket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class CleanerKeepAliveFactory implements KeepAliveMessageFactory{

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if(message instanceof HeartbeatMCPPacket){
			return true;
		}
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		//被动的心跳包，直接返回false
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		//被动的心跳包，直接返回null
		return null;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		// TODO Auto-generated method stub
		return null;
	}

}
