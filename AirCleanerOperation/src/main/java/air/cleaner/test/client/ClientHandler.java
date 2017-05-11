package air.cleaner.test.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import air.cleaner.model.MCPPacket;
import air.cleaner.utils.ByteUtil;

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
				int cid = ByteUtil.byteArrayToInt(((MCPPacket) message).getCID());
				if(cid == 1){
					((MCPPacket) message).setDATA(ByteUtil.serverToByte("127.0.0.1", 0x14));
					((MCPPacket) message).calCRC();
				}else if(cid == 2){
					((MCPPacket) message).setDATA(ByteUtil.serverToByte("7000", 0x05));
					((MCPPacket) message).calCRC();
				}else if(cid == 0xFE){
					((MCPPacket) message).setDATA(ByteUtil.serverToByte("AS121WE", 0x14));
					((MCPPacket) message).calCRC();
				}else if(cid == 0xff){
					((MCPPacket) message).setDATA(ByteUtil.serverToByte("21DAS2", 0x14));
					((MCPPacket) message).calCRC();
				}else if (cid == 0x03) {
					((MCPPacket) message).setDATA(ByteUtil.intToByteArray(10, 0x02));
					((MCPPacket) message).calCRC();
				}else if (cid == 0xFD) {
					((MCPPacket) message).setDATA(ByteUtil.intToByteArray(212, 0x01));
					((MCPPacket) message).calCRC();
				}
				session.write(message);
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
