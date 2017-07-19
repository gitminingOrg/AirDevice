package air.cleaner.mina;

import java.net.InetSocketAddress;
import java.util.Calendar;

import model.HeartbeatMCPPacket;
import model.MCPPacket;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import utils.ByteUtil;
import utils.Constant;
import utils.IPUtil;
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
				String ip = IPUtil.tell(session);
				//receive heartbeat, update status
				LOG.info("heartbeat message received:"+message);
				HeartbeatMCPPacket packet = (HeartbeatMCPPacket) message;
				deviceReceiveService.updateCacheCleanerStatus(packet, ip);
				//send return packet
				packet.setLEN(new byte[]{0x20});
				packet.setDATA(currentTimeByte());
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
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
	    session.setAttribute("KEY_SESSION_CLIENT_IP", clientIP);
	    LOG.debug("sessionCreated, client IP: " + clientIP);
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOG.error("message deliever exception caught", cause);
		super.exceptionCaught(session, cause);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOG.debug("session idle");
		super.sessionIdle(session, status);
	}
	public byte[] currentTimeByte(){
		Calendar calendar = Calendar.getInstance();
		byte[] year = ByteUtil.intToByteArray(calendar.get(Calendar.YEAR) % 100, 1);
		byte[] month = ByteUtil.intToByteArray(calendar.get(Calendar.MONTH) + 1, 1);
		byte[] date = ByteUtil.intToByteArray(calendar.get(Calendar.DATE) , 1);
		byte[] hour = ByteUtil.intToByteArray(calendar.get(Calendar.HOUR_OF_DAY) , 1);
		byte[] minute = ByteUtil.intToByteArray(calendar.get(Calendar.MINUTE) , 1);
		byte[] second = ByteUtil.intToByteArray(calendar.get(Calendar.SECOND) , 1);
		byte[] reserve = new byte[26];
		
		byte[] result = ByteUtil.concatAll(year, month, date, hour, minute, second, reserve);
		return result;
	}
}
