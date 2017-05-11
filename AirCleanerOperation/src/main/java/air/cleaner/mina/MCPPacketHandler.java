package air.cleaner.mina;

import model.HeartbeatMCPPacket;
import model.MCPPacket;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import utils.ByteUtil;
import utils.Constant;
import air.cleaner.cache.SessionCacheManager;
import air.cleaner.device.service.DeviceReceiveService;

public class MCPPacketHandler extends IoHandlerAdapter{
	public static Logger LOG = LoggerFactory.getLogger(MCPPacketHandler.class);
	
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}
	@Autowired
	private SessionCacheManager sessionCacheManager;
    public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	@Override
	public void messageReceived(IoSession session, Object message){
		if(message instanceof MCPPacket){
			//update session of device in cache
			String deviceID = ByteUtil.byteToHex(((MCPPacket) message).getUID());
			sessionCacheManager.updateSession(deviceID, session);
			
			if (message instanceof HeartbeatMCPPacket) {
				//receive heartbeat, update status
				LOG.info("heartbeat message received:"+message);
				HeartbeatMCPPacket packet = (HeartbeatMCPPacket) message;
				deviceReceiveService.updateCacheCleanerStatus(packet);
				//send return packet
				packet.setLEN(new byte[]{0x01});
				packet.setDATA(new byte[]{0x00});
				packet.calCRC();
				session.write(packet);
			}else{
				//classify device info command and cleaner status command
				int command = ByteUtil.byteArrayToInt(((MCPPacket) message).getCID());
				if(Constant.deviceSet.contains(command)){
					deviceReceiveService.updateCacheDeviceInfo((MCPPacket)message);
					LOG.info("new message of device info received :" + message);
				}else if(Constant.statusSet.contains(command)){
					deviceReceiveService.updateSingleCacheCleanerStatus((MCPPacket)message);
					LOG.info("new message of cleaner status received :" + message);
				}else{
					LOG.error("unrecognized cid" + message);
				}
			}
		}else{
			LOG.error("unrecognized message" + message);
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOG.info("Message sent! : " +message);
		if (session.isClosing()) {
			LOG.error("Client has already been disconnected");
			return;
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOG.debug("session closed" + session);
		CloseFuture closeFuture = session.close(true);
		closeFuture.addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				if(future instanceof CloseFuture){
					((CloseFuture) future).setClosed();
					LOG.info("sessionClosed" + future.getSession());
				}
			}
		});
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOG.error("message deliever exception caught", cause);
		super.exceptionCaught(session, cause);
	}
}
