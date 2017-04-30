package client;

import model.MCPPacket;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.ByteUtil;

public class ClientHandler extends IoHandlerAdapter{
	public static Logger LOG = LoggerFactory.getLogger(ClientHandler.class);
	@Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if(message instanceof MCPPacket){
        	int command = ByteUtil.byteArrayToInt(((MCPPacket) message).getCTF());
        	if (command == 0x01) {
        		LOG.debug("宝宝在设置！"+ message);
				session.write(message);
			}else if(command == 0x00){
				
			}
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    	System.out.println("发送的消息是："+message.toString());        
        //super.messageSent(session, message);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }  
}
